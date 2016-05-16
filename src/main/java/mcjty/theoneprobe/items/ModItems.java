package mcjty.theoneprobe.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static Probe probe;

    public static void init() {
        probe = new Probe();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        probe.initModel();
    }
}
