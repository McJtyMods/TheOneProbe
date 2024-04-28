package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record PacketReturnEntityInfo(UUID uuid, ProbeInfo probeInfo) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(TheOneProbe.MODID, "returnentityinfo");
    public static final CustomPacketPayload.Type<PacketReturnEntityInfo> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketReturnEntityInfo> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, PacketReturnEntityInfo::uuid,
            ProbeInfo.STREAM_CODEC, PacketReturnEntityInfo::probeInfo,
            PacketReturnEntityInfo::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketReturnEntityInfo create(UUID uuid, ProbeInfo probeInfo) {
        return new PacketReturnEntityInfo(uuid, probeInfo);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            OverlayRenderer.registerProbeInfo(uuid, probeInfo);
        });
    }

}
