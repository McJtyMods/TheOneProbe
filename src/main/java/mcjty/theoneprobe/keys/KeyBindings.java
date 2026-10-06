package mcjty.theoneprobe.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class KeyBindings {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("theoneprobe", "keys"));

    public static KeyMapping toggleLiquids;
    public static KeyMapping toggleVisible;

    public static void init() {
        toggleLiquids = new KeyMapping("key.toggleLiquids", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CATEGORY);
        toggleVisible = new KeyMapping("key.toggleVisible", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CATEGORY);
    }
}
