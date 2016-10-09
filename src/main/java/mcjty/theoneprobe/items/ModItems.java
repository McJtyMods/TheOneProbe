package mcjty.theoneprobe.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(probe, 1), "C  ", " n ", "  r", 'C', Items.COMPARATOR, 'n', "nuggetGold", 'r', "dustRedstone"));
    }
}
