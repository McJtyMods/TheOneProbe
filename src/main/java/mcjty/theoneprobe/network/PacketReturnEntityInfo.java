package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.UUID;

public record PacketReturnEntityInfo(UUID uuid, ProbeInfo probeInfo) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(TheOneProbe.MODID, "returnentityinfo");

    public static PacketReturnEntityInfo read(FriendlyByteBuf buf) {
        var uuid = buf.readUUID();
        var probeInfo = buf.readNullable(ProbeInfo::new);
        return new PacketReturnEntityInfo(uuid, probeInfo);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeNullable(probeInfo, (buf1, probeInfo1) -> probeInfo1.toBytes(buf1));
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            OverlayRenderer.registerProbeInfo(uuid, probeInfo);
        });
    }
}
