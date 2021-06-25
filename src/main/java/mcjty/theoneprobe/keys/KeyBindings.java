package mcjty.theoneprobe.keys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {

    public static KeyBinding toggleLiquids;
    public static KeyBinding toggleVisible;

    public static void init() {
        toggleLiquids = new KeyBinding("key.toggleLiquids", KeyConflictContext.IN_GAME, InputMappings.UNKNOWN, "key.categories.theoneprobe");
        toggleVisible = new KeyBinding("key.toggleVisible", KeyConflictContext.IN_GAME, InputMappings.UNKNOWN, "key.categories.theoneprobe");
        ClientRegistry.registerKeyBinding(toggleLiquids);
        ClientRegistry.registerKeyBinding(toggleVisible);
    }
}
