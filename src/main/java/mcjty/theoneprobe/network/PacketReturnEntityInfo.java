package mcjty.theoneprobe.network;

import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketReturnEntityInfo {

    private UUID uuid;
    private ProbeInfo probeInfo;

    public PacketReturnEntityInfo(PacketBuffer buf) {
        uuid = buf.readUUID();
        if (buf.readBoolean()) {
            probeInfo = new ProbeInfo();
            probeInfo.fromBytes(buf);
        } else {
            probeInfo = null;
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUUID(uuid);
        if (probeInfo != null) {
            buf.writeBoolean(true);
            probeInfo.toBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    public PacketReturnEntityInfo() {
    }

    public PacketReturnEntityInfo(UUID uuid, ProbeInfo probeInfo) {
        this.uuid = uuid;
        this.probeInfo = probeInfo;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            OverlayRenderer.registerProbeInfo(uuid, probeInfo);
        });
        ctx.get().setPacketHandled(true);
    }

}
