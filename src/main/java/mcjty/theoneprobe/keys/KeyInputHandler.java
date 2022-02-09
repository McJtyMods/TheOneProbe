package mcjty.theoneprobe.keys;

import mcjty.theoneprobe.config.Config;

public class KeyInputHandler {

    public static void onKeyInput(int key, int scancode, int action, int mods) {
        if (KeyBindings.toggleLiquids.consumeClick()) {
            Config.setLiquids(!Config.showLiquids.get());
        } else if (KeyBindings.toggleVisible.consumeClick()) {
            if (!Config.holdKeyToMakeVisible.get()) {
                Config.setVisible(!Config.isVisible.get());
            }
        }
    }
}
