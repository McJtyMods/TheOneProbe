package mcjty.theoneprobe.items;

public class ProbeHelmetRecipeFactory {}/*implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        ShapelessOreRecipe recipe = ShapelessOreRecipe.factory(context, json);

        return new HelmetRecipe(new ResourceLocation("theoneprobe", "probe_helmet"), recipe.getRecipeOutput(), recipe.getIngredients());
    }

    public static class HelmetRecipe extends ShapelessOreRecipe {
        public HelmetRecipe(ResourceLocation group, ItemStack result, NonNullList<Ingredient> ingredients) {
            super(group, ingredients, result);
        }

        @Nonnull
        @Override
        public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventory) {
            ItemStack result = super.getCraftingResult(inventory);
            NBTTagCompound tc = new NBTTagCompound();
            tc.setInteger(ModItems.PROBETAG, 1);
            result.setTagCompound(tc);
            return result;
        }
    }
}
*/