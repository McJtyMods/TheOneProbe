package mcjty.theoneprobe.items;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class AddProbeTagRecipeSerializer implements RecipeSerializer<AddProbeTagRecipe> {

    private final ShapedRecipe.Serializer serializer = new ShapedRecipe.Serializer();

    @Override
    public AddProbeTagRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ShapedRecipe recipe = serializer.fromJson(recipeId, json);
        return new AddProbeTagRecipe(recipe);
    }

    @Override
    public AddProbeTagRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        ShapedRecipe recipe = serializer.fromNetwork(recipeId, buffer);
        return new AddProbeTagRecipe(recipe);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, AddProbeTagRecipe recipe) {
        serializer.toNetwork(buffer, recipe.getRecipe());
    }
}
