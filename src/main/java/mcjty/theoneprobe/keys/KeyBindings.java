package mcjty.theoneprobe.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {

    public static KeyMapping toggleLiquids;
    public static KeyMapping toggleVisible;

    public static void init() {
        toggleLiquids = new KeyMapping("key.toggleLiquids", KeyConflictContext.IN_GAME, InputConstants.UNKNOWN, "key.categories.theoneprobe");
        toggleVisible = new KeyMapping("key.toggleVisible", KeyConflictContext.IN_GAME, InputConstants.UNKNOWN, "key.categories.theoneprobe");
        ClientRegistry.registerKeyBinding(toggleLiquids);
        ClientRegistry.registerKeyBinding(toggleVisible);
    }
}
