package mcjty.theoneprobe.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class KeyBindings {

    public static KeyMapping toggleLiquids;
    public static KeyMapping toggleVisible;

    public static void init() {
        toggleLiquids = new KeyMapping("key.toggleLiquids", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "key.categories.theoneprobe");
        toggleVisible = new KeyMapping("key.toggleVisible", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "key.categories.theoneprobe");
    }
}
