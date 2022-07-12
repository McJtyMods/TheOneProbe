package mcjty.theoneprobe;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.EXTENDED;
import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.NORMAL;

public class Tools {


    public static String getModName(EntityType<?> entry) {
        ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(entry);
        String modId = registryName == null ? "minecraft" : registryName.getNamespace();
        return ModList.get().getModContainerById(modId)
                .map(mod -> mod.getModInfo().getDisplayName())
                .orElse(StringUtils.capitalize(modId));
    }

    public static String getModName(Block entry) {
        ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(entry);
        String modId = registryName == null ? "minecraft" : registryName.getNamespace();
        return ModList.get().getModContainerById(modId)
                .map(mod -> mod.getModInfo().getDisplayName())
                .orElse(StringUtils.capitalize(modId));
    }

    public static boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == NORMAL || (cfg == EXTENDED && mode == ProbeMode.EXTENDED);
    }
}
