package mcjty.theoneprobe.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyBindings {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("theoneprobe", "keys"));

    public static KeyMapping toggleLiquids;
    public static KeyMapping toggleVisible;

    public static void init() {
        toggleLiquids = new KeyMapping("key.toggleLiquids", KeyConflictContext.IN_GAME, InputConstants.UNKNOWN, CATEGORY);
        toggleVisible = new KeyMapping("key.toggleVisible", KeyConflictContext.IN_GAME, InputConstants.UNKNOWN, CATEGORY);
    }
}
