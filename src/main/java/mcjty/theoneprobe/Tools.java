package mcjty.theoneprobe;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.EXTENDED;
import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.NORMAL;

public class Tools {

    public static String getModName(Block block) {
        ResourceLocation itemResourceLocation = block.getRegistryName();
        String modId = itemResourceLocation.getNamespace();
        String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);
        String modName = WordUtils.capitalize(modId);
        return modName;
    }

    public static String getModName(Entity entity) {
        EntityType<?> type = entity.getType();
        if (type.getRegistryName() == null) {
            return "Minecraft";
        }
        return WordUtils.capitalize(type.getRegistryName().getNamespace());
    }

    public static boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == NORMAL || (cfg == EXTENDED && mode == ProbeMode.EXTENDED);
    }
}
