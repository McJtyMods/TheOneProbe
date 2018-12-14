package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.fabricmc.fabric.networking.PacketContext;
import net.minecraft.util.PacketByteBuf;

import java.util.UUID;
import java.util.function.BiConsumer;

public class PacketReturnEntityInfo /*implements IMessage*/ {

    private UUID uuid;
    private ProbeInfo probeInfo;

//    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        if (buf.readBoolean()) {
            probeInfo = new ProbeInfo();
            probeInfo.fromBytes(buf);
        } else {
            probeInfo = null;
        }
    }

//    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
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

    public static class Handler implements BiConsumer<PacketContext, PacketByteBuf> {
        @Override
        public void accept(PacketContext context, PacketByteBuf packetByteBuf) {
            PacketReturnEntityInfo packet = new PacketReturnEntityInfo();
            packet.fromBytes(packetByteBuf);
            context.getTaskQueue().execute(() -> handle(context, packet));

        }

        public void handle(PacketContext context, PacketReturnEntityInfo message) {
            OverlayRenderer.registerProbeInfo(message.uuid, message.probeInfo);
        }
    }
}
