package mcjty.theoneprobe.keys;

import mcjty.theoneprobe.config.Config;

public class KeyInputHandler {

    public void onKeyInput() {
        if (KeyBindings.toggleLiquids.consumeClick()) {
            Config.setLiquids(!Config.showLiquids.get());
        } else if (KeyBindings.toggleVisible.consumeClick()) {
            if (!Config.holdKeyToMakeVisible.get()) {
                Config.setVisible(!Config.isVisible.get());
            }
        }
    }
}
