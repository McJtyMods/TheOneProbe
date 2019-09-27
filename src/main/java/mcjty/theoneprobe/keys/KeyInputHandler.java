package mcjty.theoneprobe.keys;

import mcjty.theoneprobe.config.Config;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.toggleLiquids.isPressed()) {
            Config.setLiquids(!Config.showLiquids.get());
        } else if (KeyBindings.toggleVisible.isPressed()) {
            if (!Config.holdKeyToMakeVisible.get()) {
                Config.setVisible(!Config.isVisible.get());
            }
        }
    }
}
