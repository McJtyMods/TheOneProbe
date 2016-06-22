package mcjty.theoneprobe.items;

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
        NBTTagCompound tc = new NBTTagCompound();
        tc.setInteger(ModItems.PROBETAG, 1);
        result.setTagCompound(tc);
        return result;
    }
}
