package mcjty.theoneprobe.network;

import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class PacketOpenGui {

    public static final int GUI_CONFIG = 0;
    public static final int GUI_NOTE = 1;

    private int gui;

    public PacketOpenGui(FriendlyByteBuf buf) {
        gui = buf.readInt();
    }

    public FriendlyByteBuf toBytes() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(gui);
        return buf;
    }

    public PacketOpenGui(int gui) {
        this.gui = gui;
    }

    public void handle(Minecraft client) {
        if (gui == GUI_CONFIG) {
            client.execute(GuiConfig::open);
        } else {
           client.execute(GuiNote::open);
        }
    }
}
