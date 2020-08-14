package mcjty.theoneprobe.network;

import io.netty.buffer.Unpooled;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDHARD;

public class PacketGetInfo  {

    private RegistryKey<World> dim;
    private BlockPos pos;
    private ProbeMode mode;
    private Direction sideHit;
    private Vector3d hitVec;
    @Nonnull private ItemStack pickBlock;

    public PacketGetInfo(PacketBuffer buf) {
        dim = RegistryKey.func_240903_a_(Registry.WORLD_KEY, buf.readResourceLocation());
        pos = buf.readBlockPos();
        mode = ProbeMode.values()[buf.readByte()];
        byte sideByte = buf.readByte();
        if (sideByte == 127) {
            sideHit = null;
        } else {
            sideHit = Direction.values()[sideByte];
        }
        if (buf.readBoolean()) {
            hitVec = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
        pickBlock = buf.readItemStack();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeResourceLocation(dim.func_240901_a_());
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

        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        buffer.writeItemStack(pickBlock);
        if (buffer.writerIndex() <= Config.maxPacketToServer.get()) {
            buf.writeBytes(buffer);
        } else {
            ItemStack copy = new ItemStack(pickBlock.getItem(), pickBlock.getCount());
            buf.writeItemStack(copy);
        }
    }

    public PacketGetInfo() {
    }

    public PacketGetInfo(RegistryKey<World> dim, BlockPos pos, ProbeMode mode, RayTraceResult mouseOver, @Nonnull ItemStack pickBlock) {
        this.dim = dim;
        this.pos = pos;
        this.mode = mode;
        this.sideHit = ((BlockRayTraceResult)mouseOver).getFace();
        this.hitVec = mouseOver.getHitVec();
        this.pickBlock = pickBlock;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld world = ctx.get().getSender().server.getWorld(dim);
            if (world != null) {
                ProbeInfo probeInfo = getProbeInfo(ctx.get().getSender(),
                        mode, world, pos, sideHit, hitVec, pickBlock);
                PacketHandler.INSTANCE.sendTo(new PacketReturnInfo(dim, pos, probeInfo), ctx.get().getSender().connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static ProbeInfo getProbeInfo(PlayerEntity player, ProbeMode mode, World world, BlockPos blockPos, Direction sideHit, Vector3d hitVec, @Nonnull ItemStack pickBlock) {
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

        BlockState state = world.getBlockState(blockPos);
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
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
                probeInfo.text(CompoundText.create().style(LABEL).text("Error: ").style(ERROR).text(provider.getID()).get());
            }
        }
        return probeInfo;
    }

}
