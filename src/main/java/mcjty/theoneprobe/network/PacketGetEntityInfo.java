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

import java.util.UUID;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDHARD;

public record PacketGetEntityInfo(ResourceKey<Level> dim, UUID uuid, ProbeMode mode, Vec3 hitVec) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(TheOneProbe.MODID, "getentityinfo");

    public static PacketGetEntityInfo create(ResourceKey<Level> dim, Entity entity, ProbeMode mode, HitResult mouseOver) {
        return new PacketGetEntityInfo(dim, entity.getUUID(), mode, mouseOver.getLocation());
    }

    public static PacketGetEntityInfo read(FriendlyByteBuf buf) {
        var dim = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        var uuid = buf.readUUID();
        var mode = buf.readEnum(ProbeMode.class);
        var hitVec = buf.readNullable(FriendlyByteBuf::readVec3);
        return new PacketGetEntityInfo(dim, uuid, mode, hitVec);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dim.location());
        buf.writeUUID(uuid);
        buf.writeEnum(mode);
        buf.writeNullable(hitVec, FriendlyByteBuf::writeVec3);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(player -> {
                var level = (ServerLevel)player.level();
                var entity = level.getEntity(uuid);
                if (entity != null) {
                    ProbeInfo probeInfo = getProbeInfo(player, mode, level, entity, hitVec);
                    PacketDistributor.PLAYER.with((ServerPlayer) player).send(new PacketReturnEntityInfo(uuid, probeInfo));
                }
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
            var rl = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
            for (var predicate : Config.getEntityBlacklist()) {
                if (predicate.test(rl)) {
                    return null;
                }
            }
        }

        var probeInfo = TheOneProbe.theOneProbeImp.create();
        var data = new ProbeHitEntityData(hitVec);

        var probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        var configProviders = TheOneProbe.theOneProbeImp.getConfigProviders();
        for (var configProvider : configProviders) {
            configProvider.getProbeConfig(probeConfig, player, world, entity, data);
        }
        Config.setRealConfig(probeConfig);

        var entityProviders = TheOneProbe.theOneProbeImp.getEntityProviders();
        for (var provider : entityProviders) {
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
