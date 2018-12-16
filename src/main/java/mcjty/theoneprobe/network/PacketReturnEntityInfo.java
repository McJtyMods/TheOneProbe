package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.fabricmc.fabric.networking.PacketContext;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class PacketReturnEntityInfo implements IPacket {

    private UUID uuid;
    private ProbeInfo probeInfo;

    public static final Identifier RETURN_ENTITY_INFO = new Identifier(TheOneProbe.MODID, "return_entity_info");

    @Override
    public Identifier getId() {
        return RETURN_ENTITY_INFO;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        if (buf.readBoolean()) {
            probeInfo = new ProbeInfo();
            probeInfo.fromBytes(buf);
        } else {
            probeInfo = null;
        }
    }

    @Override
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

    public static class Handler extends MessageHandler<PacketReturnEntityInfo> {

        @Override
        protected PacketReturnEntityInfo createPacket() {
            return new PacketReturnEntityInfo();
        }

        @Override
        public void handle(PacketContext context, PacketReturnEntityInfo message) {
            OverlayRenderer.registerProbeInfo(message.uuid, message.probeInfo);
        }
    }
}
