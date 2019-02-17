package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PacketReturnInfo implements IPacket {

    private int dim;
    private BlockPos pos;
    private ProbeInfo probeInfo;

    public static final Identifier RETURN_INFO = new Identifier(TheOneProbe.MODID, "return_info");

    @Override
    public Identifier getId() {
        return RETURN_INFO;
    }

    @Override
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

    @Override
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

    public static class Handler extends MessageHandler<PacketReturnInfo> {

        @Override
        protected PacketReturnInfo createPacket() {
            return new PacketReturnInfo();
        }

        @Override
        public void handle(PacketContext context, PacketReturnInfo message) {
            OverlayRenderer.registerProbeInfo(message.dim, message.pos, message.probeInfo);
        }
    }
}
