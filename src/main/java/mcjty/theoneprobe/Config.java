package mcjty.theoneprobe;


import net.minecraftforge.common.config.Configuration;

public class Config {
    public static String CATEGORY_THEONEPROBE = "theoneprobe";

    public static boolean needsProbe = true;
    public static int timeout = 200;

    public static void init(Configuration cfg) {
        needsProbe = cfg.getBoolean("needsProbe", CATEGORY_THEONEPROBE, needsProbe, "If true the probe is needed to show the tooltip. If false the tooltip shows all the time");
        timeout = cfg.getInt("timeout", CATEGORY_THEONEPROBE, timeout, 10, 100000, "The amount of milliseconds to wait before updating probe information from the server");
    }
}
