package mcjty.theoneprobe.items;

import mcjty.theoneprobe.gui.GuiConfig;
import net.minecraft.client.Minecraft;

public class ProbeClient {

    public static void displayProbeGui() {
        Minecraft.getInstance().displayGuiScreen(new GuiConfig());
//            player.openGui(TheOneProbe.instance, GuiProxy.GUI_CONFIG, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
//            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
