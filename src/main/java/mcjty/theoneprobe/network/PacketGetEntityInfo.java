package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitEntityData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDHARD;

public class PacketGetEntityInfo {

    private RegistryKey<World> dim;
    private UUID uuid;
    private ProbeMode mode;
    private Vector3d hitVec;

    public PacketGetEntityInfo(PacketBuffer buf) {
        dim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buf.readResourceLocation());
        uuid = buf.readUniqueId();
        mode = ProbeMode.values()[buf.readByte()];
        if (buf.readBoolean()) {
            hitVec = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeResourceLocation(dim.getLocation());
        buf.writeUniqueId(uuid);
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

    public PacketGetEntityInfo(RegistryKey<World> dim, ProbeMode mode, RayTraceResult mouseOver, Entity entity) {
        this.dim = dim;
        this.uuid = entity.getUniqueID();
        this.mode = mode;
        this.hitVec = mouseOver.getHitVec();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld world = ctx.get().getSender().server.getWorld(dim);
            if (world != null) {
                Entity entity = world.getEntityByUuid(uuid);
                if (entity != null) {
                    ProbeInfo probeInfo = getProbeInfo(ctx.get().getSender(), mode, world, entity, hitVec);
                    PacketHandler.INSTANCE.sendTo(new PacketReturnEntityInfo(uuid, probeInfo),
                            ctx.get().getSender().connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static ProbeInfo getProbeInfo(PlayerEntity player, ProbeMode mode, World world, Entity entity, Vector3d hitVec) {
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
