package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketReturnEntityInfo implements IMessage {

    private UUID uuid;
    private ProbeInfo probeInfo;

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        probeInfo = new ProbeInfo();
        probeInfo.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        probeInfo.toBytes(buf);
    }

    public PacketReturnEntityInfo() {
    }

    public PacketReturnEntityInfo(UUID uuid, ProbeInfo probeInfo) {
        this.uuid = uuid;
        this.probeInfo = probeInfo;
    }

    public static class Handler implements IMessageHandler<PacketReturnEntityInfo, IMessage> {
        @Override
        public IMessage onMessage(PacketReturnEntityInfo message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketReturnEntityInfo message, MessageContext ctx) {
            OverlayRenderer.registerProbeInfo(message.uuid, message.probeInfo);
        }
    }
}
