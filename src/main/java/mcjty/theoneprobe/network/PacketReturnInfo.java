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
        ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        BlockPos pos = buf.readBlockPos();
        ProbeInfo probeInfo;
        if (buf.readBoolean()) {
            probeInfo = new ProbeInfo();
            probeInfo.fromBytes(buf);
        } else {
            probeInfo = null;
        }
        return new PacketReturnInfo(dim, pos, probeInfo);
    }

    public static PacketReturnInfo create(ResourceKey<Level> dim, BlockPos pos, ProbeInfo probeInfo) {
        return new PacketReturnInfo(dim, pos, probeInfo);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dim.location());
        buf.writeBlockPos(pos);
        if (probeInfo != null) {
            buf.writeBoolean(true);
            probeInfo.toBytes(buf);
        } else {
            buf.writeBoolean(false);
        }
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
