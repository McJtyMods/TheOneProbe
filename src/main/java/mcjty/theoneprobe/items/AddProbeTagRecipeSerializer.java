package mcjty.theoneprobe.items;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class AddProbeTagRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AddProbeTagRecipe> {

    private final ShapedRecipe.Serializer serializer = new ShapedRecipe.Serializer();

    @Override
    public AddProbeTagRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ShapedRecipe recipe = serializer.fromJson(recipeId, json);
        return new AddProbeTagRecipe(recipe);
    }

    @Override
    public AddProbeTagRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        ShapedRecipe recipe = serializer.fromNetwork(recipeId, buffer);
        return new AddProbeTagRecipe(recipe);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, AddProbeTagRecipe recipe) {
        serializer.toNetwork(buffer, recipe.getRecipe());
    }
}
