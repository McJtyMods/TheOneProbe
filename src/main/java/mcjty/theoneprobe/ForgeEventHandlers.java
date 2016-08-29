package mcjty.theoneprobe;

import mcjty.theoneprobe.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        Config.setupStyleConfig(TheOneProbe.config);
        Config.updateDefaultOverlayStyle();

        if (TheOneProbe.config.hasChanged()) {
            TheOneProbe.config.save();
        }
    }


}