package mcjty.theoneprobe.config;


import mcjty.theoneprobe.TheOneProbe;
import net.minecraftforge.fml.config.ModConfig;

import static net.minecraftforge.ForgeConfigAPIPort.CORE;

public class ConfigReload {

    public static void onLoad(final ModConfig modConfig) {
        TheOneProbe.logger.debug("Loaded {} config file {}", TheOneProbe.MODID, modConfig.getFileName());
        Config.resolveConfigs();

    }

    public static void onFileChange(final ModConfig modConfig) {
        TheOneProbe.logger.fatal(CORE, "{} config just got changed on the file system!", TheOneProbe.MODID);
        Config.resolveConfigs();
    }


}
