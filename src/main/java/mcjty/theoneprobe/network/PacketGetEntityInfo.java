package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitEntityData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDHARD;

public record PacketGetEntityInfo(ResourceKey<Level> dim, UUID uuid, ProbeMode mode, Vec3 hitVec) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(TheOneProbe.MODID, "getentityinfo");

    public static PacketGetEntityInfo create(FriendlyByteBuf buf) {
        ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        UUID uuid = buf.readUUID();
        ProbeMode mode = ProbeMode.values()[buf.readByte()];
        Vec3 hitVec = null;
        if (buf.readBoolean()) {
            hitVec = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
        return new PacketGetEntityInfo(dim, uuid, mode, hitVec);
    }

    public static PacketGetEntityInfo create(ResourceKey<Level> dim, ProbeMode mode, HitResult mouseOver, Entity entity) {
        return new PacketGetEntityInfo(dim, entity.getUUID(), mode, mouseOver.getLocation());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
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

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.level().ifPresent(level -> {
                ctx.player().ifPresent(player -> {
                    ServerLevel world = (ServerLevel) level;
                    Entity entity = world.getEntity(uuid);
                    if (entity != null) {
                        ProbeInfo probeInfo = getProbeInfo(player, mode, world, entity, hitVec);
                        PacketDistributor.PLAYER.with((ServerPlayer) player).send(PacketReturnEntityInfo.create(uuid, probeInfo));
                    }
                });
            });
        })
        .exceptionally(e -> {
            ctx.packetHandler().disconnect(Component.translatable("theoneprobe.networking.failed", e.getMessage()));
            return null;
        });
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
            ResourceLocation rl = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
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
