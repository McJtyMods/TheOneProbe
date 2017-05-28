package mcjty.theoneprobe.items;

import mcjty.theoneprobe.api.IProbeItem;
import mcjty.theoneprobe.api.ProbeChecker;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class AddProbeRecipe extends ShapedRecipes {

    public AddProbeRecipe(Item helmet, Item output) {
        super(2, 1, new ItemStack[] { new ItemStack(helmet), new ItemStack(ModItems.probe) }, new ItemStack(output));
    }

    @Nullable
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack result = super.getCraftingResult(inv);
        if(result != null && !(result.getItem() instanceof IProbeItem)) { //Only set probetag if result item is not IProbeItem
            NBTTagCompound tc = new NBTTagCompound();
            tc.setInteger(ProbeChecker.PROBETAG, 1);
            result.setTagCompound(tc);
        }
        return result;
    }
}
