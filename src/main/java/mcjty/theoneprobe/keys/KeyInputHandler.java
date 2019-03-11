package mcjty.theoneprobe.keys;

import mcjty.theoneprobe.config.ConfigSetup;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.toggleLiquids.isPressed()) {
            ConfigSetup.setLiquids(!ConfigSetup.showLiquids);
        } else if (KeyBindings.toggleVisible.isPressed()) {
            if (!ConfigSetup.holdKeyToMakeVisible) {
                ConfigSetup.setVisible(!ConfigSetup.isVisible);
            }
//        } else if (KeyBindings.generateLag.isPressed()) {
//            PacketHandler.INSTANCE.sendToServer(new PacketGenerateLag());
        }
    }
}
