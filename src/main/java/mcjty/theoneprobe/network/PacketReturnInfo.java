package mcjty.theoneprobe.network;

import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketReturnInfo {

    private DimensionType dim;
    private BlockPos pos;
    private ProbeInfo probeInfo;

    public PacketReturnInfo(PacketBuffer buf) {
        dim = DimensionType.getById(buf.readInt());
        pos = buf.readBlockPos();
        if (buf.readBoolean()) {
            probeInfo = new ProbeInfo();
            probeInfo.fromBytes(buf);
        } else {
            probeInfo = null;
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(dim.getId());
        buf.writeBlockPos(pos);
        if (probeInfo != null) {
            buf.writeBoolean(true);
            probeInfo.toBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    public PacketReturnInfo() {
    }

    public PacketReturnInfo(DimensionType dim, BlockPos pos, ProbeInfo probeInfo) {
        this.dim = dim;
        this.pos = pos;
        this.probeInfo = probeInfo;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            OverlayRenderer.registerProbeInfo(dim, pos, probeInfo);
        });
        ctx.get().setPacketHandled(true);
    }
}
