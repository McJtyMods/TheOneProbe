package mcjty.theoneprobe.keys;

import mcjty.theoneprobe.config.Config;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.toggleLiquids.isPressed()) {
            Config.setLiquids(!Config.showLiquids.get());
        } else if (KeyBindings.toggleVisible.isPressed()) {
            if (!Config.holdKeyToMakeVisible.get()) {
                Config.setVisible(!Config.isVisible.get());
            }
//        } else if (KeyBindings.generateLag.isPressed()) {
//            PacketHandler.INSTANCE.sendToServer(new PacketGenerateLag());
        }
    }
}
