package mcjty.theoneprobe.items;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.Collections;

public class AddProbeRecipe extends ShapedRecipes {

    public AddProbeRecipe(Item helmet, Item output) {
        //@todo
//        super("todo", 2, 1, new ItemStack[] { new ItemStack(helmet), new ItemStack(ModItems.probe) }, new ItemStack(output));
        super("todo", 2, 1, NonNullList.create(), new ItemStack(output));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack result = super.getCraftingResult(inv);
        NBTTagCompound tc = new NBTTagCompound();
        tc.setInteger(ModItems.PROBETAG, 1);
        result.setTagCompound(tc);
        return result;
    }
}
