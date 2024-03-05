package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record PacketReturnInfo(ResourceKey<Level> dim, BlockPos pos, ProbeInfo probeInfo) implements CustomPacketPayload {

    public static ResourceLocation ID = new ResourceLocation(TheOneProbe.MODID, "returninfo");

    public static PacketReturnInfo create(FriendlyByteBuf buf) {
        var dim = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        var pos = buf.readBlockPos();
        var probeInfo = buf.readNullable(ProbeInfo::new);
        return new PacketReturnInfo(dim, pos, probeInfo);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dim.location());
        buf.writeBlockPos(pos);
        buf.writeNullable(probeInfo, (buf1, probeInfo1) -> probeInfo1.toBytes(buf1));
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            OverlayRenderer.registerProbeInfo(dim, pos, probeInfo);
        });
    }
}
