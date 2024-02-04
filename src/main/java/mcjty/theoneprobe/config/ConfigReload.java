package mcjty.theoneprobe.config;


import mcjty.theoneprobe.TheOneProbe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;

import static net.neoforged.fml.Logging.CORE;

@Mod.EventBusSubscriber(modid = TheOneProbe.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigReload {


    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        TheOneProbe.logger.debug("Loaded {} config file {}", TheOneProbe.MODID, configEvent.getConfig().getFileName());
        Config.resolveConfigs();

    }

    @SubscribeEvent
    public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
        TheOneProbe.logger.fatal(CORE, "{} config just got changed on the file system!", TheOneProbe.MODID);
        Config.resolveConfigs();
    }


}
