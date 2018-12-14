package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.fabricmc.fabric.networking.PacketContext;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.function.BiConsumer;

// @todo fabric
public class PacketReturnInfo /* implements IMessage*/ {

    private int dim;
    private BlockPos pos;
    private ProbeInfo probeInfo;

//    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        if (buf.readBoolean()) {
            probeInfo = new ProbeInfo();
            probeInfo.fromBytes(buf);
        } else {
            probeInfo = null;
        }
    }

//    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        if (probeInfo != null) {
            buf.writeBoolean(true);
            probeInfo.toBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    public PacketReturnInfo() {
    }

    public PacketReturnInfo(int dim, BlockPos pos, ProbeInfo probeInfo) {
        this.dim = dim;
        this.pos = pos;
        this.probeInfo = probeInfo;
    }

    public static class Handler implements BiConsumer<PacketContext, PacketByteBuf> {

        @Override
        public void accept(PacketContext context, PacketByteBuf packetByteBuf) {
            PacketReturnInfo packet = new PacketReturnInfo();
            packet.fromBytes(packetByteBuf);
            context.getTaskQueue().execute(() -> handle(context, packet));
        }

        public void handle(PacketContext context, PacketReturnInfo message) {
            OverlayRenderer.registerProbeInfo(message.dim, message.pos, message.probeInfo);
        }
    }
}
