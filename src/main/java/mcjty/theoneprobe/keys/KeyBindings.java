package mcjty.theoneprobe.keys;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class KeyBindings {

    public static KeyBinding toggleLiquids;
    public static KeyBinding toggleVisible;
//    public static KeyBinding generateLag;

    public static void init() {
        toggleLiquids = new KeyBinding("key.toggleLiquids", InputUtil.Type.KEY_KEYBOARD, 0, "key.categories.theoneprobe");  // @todo fabric KEY_L
        toggleVisible = new KeyBinding("key.toggleVisible", InputUtil.Type.KEY_KEYBOARD, 0, "key.categories.theoneprobe");  // @todo fabric Keyboard.KEY_NONE
        // @todo fabric
//        ClientRegistry.registerKeyBinding(toggleLiquids);
//        ClientRegistry.registerKeyBinding(toggleVisible);
    }
}
