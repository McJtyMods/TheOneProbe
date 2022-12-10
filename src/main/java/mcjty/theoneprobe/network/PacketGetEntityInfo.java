package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitEntityData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDHARD;

public class PacketGetEntityInfo {

    private ResourceKey<Level> dim;
    private UUID uuid;
    private ProbeMode mode;
    private Vec3 hitVec;

    public PacketGetEntityInfo(FriendlyByteBuf buf) {
        dim = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        uuid = buf.readUUID();
        mode = ProbeMode.values()[buf.readByte()];
        if (buf.readBoolean()) {
            hitVec = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dim.location());
        buf.writeUUID(uuid);
        buf.writeByte(mode.ordinal());
        if (hitVec == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeDouble(hitVec.x);
            buf.writeDouble(hitVec.y);
            buf.writeDouble(hitVec.z);
        }
    }

    public PacketGetEntityInfo() {
    }

    public PacketGetEntityInfo(ResourceKey<Level> dim, ProbeMode mode, HitResult mouseOver, Entity entity) {
        this.dim = dim;
        this.uuid = entity.getUUID();
        this.mode = mode;
        this.hitVec = mouseOver.getLocation();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerLevel world = ctx.get().getSender().server.getLevel(dim);
            if (world != null) {
                Entity entity = world.getEntity(uuid);
                if (entity != null) {
                    ProbeInfo probeInfo = getProbeInfo(ctx.get().getSender(), mode, world, entity, hitVec);
                    PacketHandler.INSTANCE.sendTo(new PacketReturnEntityInfo(uuid, probeInfo),
                            ctx.get().getSender().connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static ProbeInfo getProbeInfo(Player player, ProbeMode mode, Level world, Entity entity, Vec3 hitVec) {
        if (Config.needsProbe.get() == PROBE_NEEDEDFOREXTENDED) {
            // We need a probe only for extended information
            if (!ModItems.hasAProbeSomewhere(player)) {
                // No probe anywhere, switch EXTENDED to NORMAL
                if (mode == ProbeMode.EXTENDED) {
                    mode = ProbeMode.NORMAL;
                }
            }
        } else if (Config.needsProbe.get() == PROBE_NEEDEDHARD && !ModItems.hasAProbeSomewhere(player)) {
            // The server says we need a probe but we don't have one in our hands or on our head
            return null;
        }

        if (!Config.getEntityBlacklist().isEmpty()) {
            ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
            for (Predicate<ResourceLocation> predicate : Config.getEntityBlacklist()) {
                if (predicate.test(rl)) {
                    return null;
                }
            }
        }

        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        IProbeHitEntityData data = new ProbeHitEntityData(hitVec);

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        List<IProbeConfigProvider> configProviders = TheOneProbe.theOneProbeImp.getConfigProviders();
        for (IProbeConfigProvider configProvider : configProviders) {
            configProvider.getProbeConfig(probeConfig, player, world, entity, data);
        }
        Config.setRealConfig(probeConfig);

        List<IProbeInfoEntityProvider> entityProviders = TheOneProbe.theOneProbeImp.getEntityProviders();
        for (IProbeInfoEntityProvider provider : entityProviders) {
            try {
                provider.addProbeEntityInfo(mode, probeInfo, player, world, entity, data);
            } catch (Throwable e) {
                ThrowableIdentity.registerThrowable(e);
                probeInfo.text(CompoundText.create().style(LABEL).text("Error: ").style(ERROR).text(provider.getID()));
            }
        }
        return probeInfo;
    }

}
