package mcjty.theoneprobe.compat;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaubleTools {

    public static Item initProbeGoggle() {
        return new ProbeGoggles();
    }

    @SideOnly(Side.CLIENT)
    public static void initProbeModel(Item probeGoggle) {
        ((ProbeGoggles) probeGoggle).initModel();
    }

}
