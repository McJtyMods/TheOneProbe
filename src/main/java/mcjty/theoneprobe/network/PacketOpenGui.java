package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketOpenGui(int gui) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(TheOneProbe.MODID, "opengui");
    public static final CustomPacketPayload.Type<PacketOpenGui> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, PacketOpenGui> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PacketOpenGui::gui,
            PacketOpenGui::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final int GUI_CONFIG = 0;
    public static final int GUI_NOTE = 1;

    public static PacketOpenGui create(int gui) {
        return new PacketOpenGui(gui);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (gui == GUI_CONFIG) {
                GuiConfig.open();
            } else {
                GuiNote.open();
            }
        });
    }
}
