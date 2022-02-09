package mcjty.theoneprobe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class Recipes extends FabricRecipesProvider {

    public Recipes(FabricDataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void generateRecipes(Consumer<FinishedRecipe> exporter) {

    }

}
