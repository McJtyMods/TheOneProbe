package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketReturnInfo(ResourceKey<Level> dim, BlockPos pos, ProbeInfo probeInfo) implements CustomPacketPayload {

    public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TheOneProbe.MODID, "returninfo");
    public static CustomPacketPayload.Type<PacketReturnInfo> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketReturnInfo> CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), PacketReturnInfo::dim,
            BlockPos.STREAM_CODEC, PacketReturnInfo::pos,
            ProbeInfo.STREAM_CODEC, PacketReturnInfo::probeInfo,
            PacketReturnInfo::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketReturnInfo create(ResourceKey<Level> dim, BlockPos pos, ProbeInfo probeInfo) {
        return new PacketReturnInfo(dim, pos, probeInfo);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            OverlayRenderer.registerProbeInfo(dim, pos, probeInfo);
        });
    }
}
