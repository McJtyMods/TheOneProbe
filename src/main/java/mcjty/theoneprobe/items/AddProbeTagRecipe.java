package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.registries.ObjectHolder;

public class AddProbeTagRecipe extends AbstractRecipeAdaptor {

    @ObjectHolder(TheOneProbe.MODID + ":probe_helmet")
    public static AddProbeTagRecipeSerializer HELMET_SERIALIZER;

    public AddProbeTagRecipe(ShapedRecipe recipe) {
        super(recipe);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack result = recipe.getCraftingResult(inv);
        CompoundNBT tc = new CompoundNBT();
        tc.putInt(ModItems.PROBETAG, 1);
        result.setTag(tc);
        return result;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return HELMET_SERIALIZER;
    }
}
