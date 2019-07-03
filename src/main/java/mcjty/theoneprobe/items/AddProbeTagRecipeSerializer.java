package mcjty.theoneprobe.items;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class AddProbeTagRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AddProbeTagRecipe> {

    private final ShapedRecipe.Serializer serializer = new ShapedRecipe.Serializer();

    @Override
    public AddProbeTagRecipe read(ResourceLocation recipeId, JsonObject json) {
        ShapedRecipe recipe = serializer.read(recipeId, json);
        return new AddProbeTagRecipe(recipe);
    }

    @Override
    public AddProbeTagRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        ShapedRecipe recipe = serializer.read(recipeId, buffer);
        return new AddProbeTagRecipe(recipe);
    }

    @Override
    public void write(PacketBuffer buffer, AddProbeTagRecipe recipe) {
        serializer.write(buffer, recipe.getRecipe());
    }
}
