package mcjty.theoneprobe.keys;

import mcjty.theoneprobe.config.Config;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.toggleLiquids.isPressed()) {
            Config.setLiquids(!Config.showLiquids);
        } else if (KeyBindings.toggleVisible.isPressed()) {
            if (!Config.holdKeyToMakeVisible) {
                Config.setVisible(!Config.isVisible);
            }
//        } else if (KeyBindings.generateLag.isPressed()) {
//            PacketHandler.INSTANCE.sendToServer(new PacketGenerateLag());
        }
    }
}
