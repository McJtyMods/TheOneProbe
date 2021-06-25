package mcjty.theoneprobe.items;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class AbstractRecipeAdaptor implements ICraftingRecipe, net.minecraftforge.common.crafting.IShapedRecipe<CraftingInventory> {

    protected final ShapedRecipe recipe;

    public AbstractRecipeAdaptor(ShapedRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return recipe.canCraftInDimensions(width, height);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        return recipe.getRemainingItems(inv);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipe.getIngredients();
    }

    @Override
    public boolean isSpecial() {
        return recipe.isSpecial();
    }

    @Override
    public String getGroup() {
        return recipe.getGroup();
    }

    @Override
    public ItemStack getToastSymbol() {
        return recipe.getToastSymbol();
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    @Override
    public int getRecipeWidth() {
        return recipe.getRecipeWidth();
    }

    @Override
    public int getRecipeHeight() {
        return recipe.getRecipeHeight();
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return recipe.matches(inv, worldIn);
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        return recipe.assemble(inv);
    }

    @Override
    public ItemStack getResultItem() {
        return recipe.getResultItem();
    }

    @Override
    public ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public IRecipeType<?> getType() {
        return recipe.getType();
    }
}
