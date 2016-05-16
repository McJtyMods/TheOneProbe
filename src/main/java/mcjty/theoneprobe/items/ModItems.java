package mcjty.theoneprobe.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static Probe probe;

    public static void init() {
        probe = new Probe();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        probe.initModel();
    }

    public static void initCrafting() {
        GameRegistry.addRecipe(new ItemStack(probe, 1), "C  ", " n ", "  r", 'C', Items.COMPARATOR, 'n', Items.GOLD_NUGGET, 'r', Items.REDSTONE);
    }
}
