package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.gui.GuiConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import mcjty.theoneprobe.gui.GuiNote;

public class GuiProxy implements IGuiHandler {

    public static int GUI_NOTE = 1;
    public static int GUI_CONFIG = 2;

    @Override
    public Object getServerGuiElement(int guiid, PlayerEntity PlayerEntity, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiid, PlayerEntity PlayerEntity, World world, int x, int y, int z) {
        if (guiid == GUI_NOTE) {
            return new GuiNote();
        } else if (guiid == GUI_CONFIG) {
            return new GuiConfig();
        } else {
            return null;
        }
    }
}
