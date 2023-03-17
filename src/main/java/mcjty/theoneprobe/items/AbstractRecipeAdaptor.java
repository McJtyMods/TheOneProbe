package mcjty.theoneprobe.items;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public abstract class AbstractRecipeAdaptor implements CraftingRecipe, net.minecraftforge.common.crafting.IShapedRecipe<CraftingContainer> {

    protected final ShapedRecipe recipe;

    public AbstractRecipeAdaptor(ShapedRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return recipe.canCraftInDimensions(width, height);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
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
    public boolean matches(CraftingContainer inv, Level worldIn) {
        return recipe.matches(inv, worldIn);
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return recipe.assemble(inv, access);
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return recipe.getResultItem(access);
    }

    @Override
    public ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public RecipeType<?> getType() {
        return recipe.getType();
    }
}
