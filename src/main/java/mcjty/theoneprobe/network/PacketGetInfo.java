package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import javax.annotation.Nonnull;
import java.util.List;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDHARD;

import org.jetbrains.annotations.NotNull;

public record PacketGetInfo(ResourceKey<Level> dim, BlockPos pos, ProbeMode mode, Direction sideHit, Vec3 hitVec, @NotNull ItemStack pickBlock) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(TheOneProbe.MODID, "getinfo");

    public static PacketGetInfo create(ResourceKey<Level> dim, BlockPos pos, ProbeMode mode, HitResult mouseOver, @NotNull ItemStack pickBlock) {
        var sideHit = ((BlockHitResult)mouseOver).getDirection();
        return new PacketGetInfo(dim, pos, mode, sideHit, mouseOver.getLocation(), pickBlock);
    }

    public static PacketGetInfo read(FriendlyByteBuf buf) {
        var dim = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        var pos = buf.readBlockPos();
        var mode = buf.readEnum(ProbeMode.class);
        var sideHit = buf.readNullable(buf1 -> buf1.readEnum(Direction.class));
        var hitVec = buf.readNullable(FriendlyByteBuf::readVec3);
        var pickBlock = buf.readItem();
        return new PacketGetInfo(dim, pos, mode, sideHit, hitVec, pickBlock);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dim.location());
        buf.writeBlockPos(pos);
        buf.writeEnum(mode);
        buf.writeNullable(sideHit, FriendlyByteBuf::writeEnum);
        buf.writeNullable(hitVec, FriendlyByteBuf::writeVec3);
        buf.writeItem(pickBlock);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(player -> {
                var level = player.level();
                var probeInfo = getProbeInfo(player, mode, level, pos, sideHit, hitVec, pickBlock);
                PacketDistributor.PLAYER.with((ServerPlayer) player).send(new PacketReturnInfo(dim, pos, probeInfo));
            });
        });
    }

    private static ProbeInfo getProbeInfo(Player player, ProbeMode mode, Level world, BlockPos blockPos, Direction sideHit, Vec3 hitVec, @Nonnull ItemStack pickBlock) {
        if (Config.needsProbe.get() == PROBE_NEEDEDFOREXTENDED) {
            // We need a probe only for extended information
            if (!ModItems.hasAProbeSomewhere(player)) {
                // No probe anywhere, switch EXTENDED to NORMAL
                if (mode == ProbeMode.EXTENDED) {
                    mode = ProbeMode.NORMAL;
                }
            }
        } else if (Config.needsProbe.get() == PROBE_NEEDEDHARD && !ModItems.hasAProbeSomewhere(player)) {
            // The server says we need a probe but we don't have one in our hands
            return null;
        }

        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        if (world.hasChunkAt(blockPos)) {
            BlockState state = world.getBlockState(blockPos);
            IProbeHitData data = new ProbeHitData(blockPos, hitVec, sideHit, pickBlock);

            IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
            List<IProbeConfigProvider> configProviders = TheOneProbe.theOneProbeImp.getConfigProviders();
            for (IProbeConfigProvider configProvider : configProviders) {
                configProvider.getProbeConfig(probeConfig, player, world, state, data);
            }
            Config.setRealConfig(probeConfig);

            List<IProbeInfoProvider> providers = TheOneProbe.theOneProbeImp.getProviders();
            for (IProbeInfoProvider provider : providers) {
                try {
                    provider.addProbeInfo(mode, probeInfo, player, world, state, data);
                } catch (Throwable e) {
                    ThrowableIdentity.registerThrowable(e);
                    probeInfo.text(CompoundText.create().style(LABEL).text("Error: ").style(ERROR).text(provider.getID().toString()));
                }
            }
        } else {
            // Block is not loaded. Something fishy is going on
            probeInfo.text(CompoundText.create().style(LABEL).text("Error: ").style(ERROR).text("Chunk not loaded!"));
        }
        return probeInfo;
    }

}
