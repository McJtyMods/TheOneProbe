package mcjty.theoneprobe.config;

import mcjty.theoneprobe.gui.DummyConfigContainer;
import mcjty.theoneprobe.gui.GuiConfig;
import net.minecraft.entity.player.PlayerInventory;

public class TopModConfigGui extends GuiConfig {

    public TopModConfigGui(DummyConfigContainer screenContainer, PlayerInventory inv) {
        super(screenContainer, inv);
    }
        // 1.13
//        super(parentScreen, new ConfigElement(TheOneProbe.config.getCategory(Config.CATEGORY_CLIENT)).getChildElements(),
//                TheOneProbe.MODID, false, false, "The One Probe Config");
}
