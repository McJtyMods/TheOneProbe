package mcjty.theoneprobe.keys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {

    public static KeyBinding toggleLiquids;
    public static KeyBinding toggleVisible;

    public static void init() {
        toggleLiquids = new KeyBinding("key.toggleLiquids", KeyConflictContext.IN_GAME, Keyboard.KEY_L, "key.categories.theoneprobe");
        toggleVisible = new KeyBinding("key.toggleVisible", KeyConflictContext.IN_GAME, Keyboard.KEY_V, "key.categories.theoneprobe");
        ClientRegistry.registerKeyBinding(toggleLiquids);
        ClientRegistry.registerKeyBinding(toggleVisible);
    }
}
