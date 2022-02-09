package mcjty.theoneprobe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;

public class LootTables extends LootTableProvider {

    public LootTables(FabricDataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    public void run(HashCache cache) {
    }
}
