package mcjty.theoneprobe.network;

import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketReturnEntityInfo {

    private UUID uuid;
    private ProbeInfo probeInfo;

    public PacketReturnEntityInfo(FriendlyByteBuf buf) {
        uuid = buf.readUUID();
        if (buf.readBoolean()) {
            probeInfo = new ProbeInfo();
            probeInfo.fromBytes(buf);
        } else {
            probeInfo = null;
        }
    }

    public FriendlyByteBuf toBytes() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUUID(uuid);
        if (probeInfo != null) {
            buf.writeBoolean(true);
            probeInfo.toBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
        return buf;
    }

    public PacketReturnEntityInfo() {
    }

    public PacketReturnEntityInfo(UUID uuid, ProbeInfo probeInfo) {
        this.uuid = uuid;
        this.probeInfo = probeInfo;
    }

    public void handle(Minecraft client) {
        client.execute(() -> {
            OverlayRenderer.registerProbeInfo(uuid, probeInfo);
        });
    }

}
