package mcjty.theoneprobe.network;

import io.netty.buffer.Unpooled;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDHARD;

public class PacketGetInfo  {

    private ResourceKey<Level> dim;
    private BlockPos pos;
    private ProbeMode mode;
    private Direction sideHit;
    private Vec3 hitVec;
    @Nonnull private ItemStack pickBlock;

    public PacketGetInfo(FriendlyByteBuf buf) {
        dim = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
        pos = buf.readBlockPos();
        mode = ProbeMode.values()[buf.readByte()];
        byte sideByte = buf.readByte();
        if (sideByte == 127) {
            sideHit = null;
        } else {
            sideHit = Direction.values()[sideByte];
        }
        if (buf.readBoolean()) {
            hitVec = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
        pickBlock = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dim.location());
        buf.writeBlockPos(pos);
        buf.writeByte(mode.ordinal());
        buf.writeByte(sideHit == null ? 127 : sideHit.ordinal());
        if (hitVec == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeDouble(hitVec.x);
            buf.writeDouble(hitVec.y);
            buf.writeDouble(hitVec.z);
        }

        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeItem(pickBlock);
        if (buffer.writerIndex() <= Config.maxPacketToServer.get()) {
            buf.writeBytes(buffer);
        } else {
            ItemStack copy = new ItemStack(pickBlock.getItem(), pickBlock.getCount());
            buf.writeItem(copy);
        }
    }

    public PacketGetInfo() {
    }

    public PacketGetInfo(ResourceKey<Level> dim, BlockPos pos, ProbeMode mode, HitResult mouseOver, @Nonnull ItemStack pickBlock) {
        this.dim = dim;
        this.pos = pos;
        this.mode = mode;
        this.sideHit = ((BlockHitResult)mouseOver).getDirection();
        this.hitVec = mouseOver.getLocation();
        this.pickBlock = pickBlock;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerLevel world = ctx.get().getSender().server.getLevel(dim);
            if (world != null) {
                ProbeInfo probeInfo = getProbeInfo(ctx.get().getSender(),
                        mode, world, pos, sideHit, hitVec, pickBlock);
                PacketHandler.INSTANCE.sendTo(new PacketReturnInfo(dim, pos, probeInfo), ctx.get().getSender().connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        ctx.get().setPacketHandled(true);
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
