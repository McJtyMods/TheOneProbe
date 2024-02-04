package mcjty.theoneprobe.network;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record PacketOpenGui(int gui) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(TheOneProbe.MODID, "opengui");

    public static final int GUI_CONFIG = 0;
    public static final int GUI_NOTE = 1;

    public static PacketOpenGui create(FriendlyByteBuf buf) {
        return new PacketOpenGui(buf.readInt());
    }

    public static PacketOpenGui create(int gui) {
        return new PacketOpenGui(gui);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(gui);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            if (gui == GUI_CONFIG) {
                GuiConfig.open();
            } else {
                GuiNote.open();
            }
        });
    }
}
