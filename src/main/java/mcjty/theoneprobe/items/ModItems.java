package mcjty.theoneprobe.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static CreativeProbe creativeProbe;
    public static Probe probe;

    public static void init() {
        probe = new Probe();
        creativeProbe = new CreativeProbe();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        probe.initModel();
        creativeProbe.initModel();
    }

    public static void initCrafting() {
        GameRegistry.addRecipe(new ItemStack(probe, 1), "C  ", " n ", "  r", 'C', Items.COMPARATOR, 'n', Items.GOLD_NUGGET, 'r', Items.REDSTONE);
    }

    public static boolean isProbe(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return stack.getItem() == probe || stack.getItem() == creativeProbe;
    }
}
