package mcjty.theoneprobe.config;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class TopModConfigGui extends GuiConfig {

    public TopModConfigGui(GuiScreen parentScreen) {
        super(parentScreen, new ConfigElement(ConfigSetup.mainConfig.getCategory(ConfigSetup.CATEGORY_CLIENT)).getChildElements(),
                TheOneProbe.MODID, false, false, "The One Probe Config");
    }
}
