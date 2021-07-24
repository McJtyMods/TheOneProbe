package mcjty.theoneprobe.config;


import mcjty.theoneprobe.TheOneProbe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import static net.minecraftforge.fml.Logging.CORE;

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
