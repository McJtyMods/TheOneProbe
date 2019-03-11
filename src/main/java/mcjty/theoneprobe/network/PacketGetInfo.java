package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.ConfigSetup.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.ConfigSetup.PROBE_NEEDEDHARD;

public class PacketGetInfo implements IMessage {

    private int dim;
    private BlockPos pos;
    private ProbeMode mode;
    private EnumFacing sideHit;
    private Vec3d hitVec;
    private ItemStack pickBlock;

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        mode = ProbeMode.values()[buf.readByte()];
        byte sideByte = buf.readByte();
        if (sideByte == 127) {
            sideHit = null;
        } else {
            sideHit = EnumFacing.values()[sideByte];
        }
        if (buf.readBoolean()) {
            hitVec = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
        pickBlock = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
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

        ByteBuf buffer = Unpooled.buffer();
        ByteBufUtils.writeItemStack(buffer, pickBlock);
        if (buffer.writerIndex() <= ConfigSetup.maxPacketToServer) {
            buf.writeBytes(buffer);
        } else {
            ItemStack copy = new ItemStack(pickBlock.getItem(), pickBlock.getCount(), pickBlock.getMetadata());
            ByteBufUtils.writeItemStack(buf, copy);
        }
    }

    public PacketGetInfo() {
    }

    public PacketGetInfo(int dim, BlockPos pos, ProbeMode mode, RayTraceResult mouseOver, ItemStack pickBlock) {
        this.dim = dim;
        this.pos = pos;
        this.mode = mode;
        this.sideHit = mouseOver.sideHit;
        this.hitVec = mouseOver.hitVec;
        this.pickBlock = pickBlock;
    }

    public static class Handler implements IMessageHandler<PacketGetInfo, IMessage> {
        @Override
        public IMessage onMessage(PacketGetInfo message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketGetInfo message, MessageContext ctx) {
            WorldServer world = DimensionManager.getWorld(message.dim);
            if (world != null) {
                ProbeInfo probeInfo = getProbeInfo(ctx.getServerHandler().player,
                        message.mode, world, message.pos, message.sideHit, message.hitVec, message.pickBlock);
                PacketHandler.INSTANCE.sendTo(new PacketReturnInfo(message.dim, message.pos, probeInfo), ctx.getServerHandler().player);
            }
        }
    }

    private static ProbeInfo getProbeInfo(EntityPlayer player, ProbeMode mode, World world, BlockPos blockPos, EnumFacing sideHit, Vec3d hitVec, ItemStack pickBlock) {
        if (ConfigSetup.needsProbe == PROBE_NEEDEDFOREXTENDED) {
            // We need a probe only for extended information
            if (!ModItems.hasAProbeSomewhere(player)) {
                // No probe anywhere, switch EXTENDED to NORMAL
                if (mode == ProbeMode.EXTENDED) {
                    mode = ProbeMode.NORMAL;
                }
            }
        } else if (ConfigSetup.needsProbe == PROBE_NEEDEDHARD && !ModItems.hasAProbeSomewhere(player)) {
            // The server says we need a probe but we don't have one in our hands
            return null;
        }

        IBlockState state = world.getBlockState(blockPos);
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        IProbeHitData data = new ProbeHitData(blockPos, hitVec, sideHit, pickBlock);

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        List<IProbeConfigProvider> configProviders = TheOneProbe.theOneProbeImp.getConfigProviders();
        for (IProbeConfigProvider configProvider : configProviders) {
            configProvider.getProbeConfig(probeConfig, player, world, state, data);
        }
        ConfigSetup.setRealConfig(probeConfig);

        List<IProbeInfoProvider> providers = TheOneProbe.theOneProbeImp.getProviders();
        for (IProbeInfoProvider provider : providers) {
            try {
                provider.addProbeInfo(mode, probeInfo, player, world, state, data);
            } catch (Throwable e) {
                ThrowableIdentity.registerThrowable(e);
                probeInfo.text(LABEL + "Error: " + ERROR + provider.getID());
            }
        }
        return probeInfo;
    }

}
