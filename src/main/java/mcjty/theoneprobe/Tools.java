package mcjty.theoneprobe;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.EXTENDED;
import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.NORMAL;

public class Tools {

    public static String getModName(Block block) {
        Identifier id = Registry.BLOCK.getId(block);
        return id.getNamespace();
    }

    public static String getModName(Entity entity) {
        Identifier id = Registry.ENTITY_TYPE.getId(entity.getType());
        if (id != null) {
            return id.getNamespace();
        } else {
            return null;
        }
    }

    public static boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == NORMAL || (cfg == EXTENDED && mode == ProbeMode.EXTENDED);
    }
}
