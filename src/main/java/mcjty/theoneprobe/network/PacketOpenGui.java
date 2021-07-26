package mcjty.theoneprobe.network;

import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenGui {

    public static int GUI_CONFIG = 0;
    public static int GUI_NOTE = 1;

    private int gui;

    public PacketOpenGui(FriendlyByteBuf buf) {
        gui = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(gui);
    }

    public PacketOpenGui(int gui) {
        this.gui = gui;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (gui == GUI_CONFIG) {
            ctx.get().enqueueWork(GuiConfig::open);
        } else {
            ctx.get().enqueueWork(GuiNote::open);
        }
        ctx.get().setPacketHandled(true);
    }
}
