package mcjty.theoneprobe;


import mcjty.theoneprobe.api.NumberFormat;
import net.minecraftforge.common.config.Configuration;

public class Config {
    public static String CATEGORY_THEONEPROBE = "theoneprobe";

    public static boolean needsProbe = true;
    public static int showRF = 1;
    public static NumberFormat rfFormat = NumberFormat.COMPACT;
    public static int timeout = 200;
    public static boolean showHarvestLevel = true;

    public static void init(Configuration cfg) {
        needsProbe = cfg.getBoolean("needsProbe", CATEGORY_THEONEPROBE, needsProbe, "If true the probe is needed to show the tooltip. If false the tooltip shows all the time");
        showRF = cfg.getInt("showRF", CATEGORY_THEONEPROBE, showRF, 0, 2, "How to display RF: 0 = do not show, 1 = show in a bar, 2 = show as text");
        int fmt = cfg.getInt("rfFormat", CATEGORY_THEONEPROBE, rfFormat.ordinal(), 0, 2, "Format for displaying RF: 0 = full, 1 = compact, 2 = comma separated");
        rfFormat = NumberFormat.values()[fmt];
        timeout = cfg.getInt("timeout", CATEGORY_THEONEPROBE, timeout, 10, 100000, "The amount of milliseconds to wait before updating probe information from the server");
        showHarvestLevel = cfg.getBoolean("showHarvestLevel", CATEGORY_THEONEPROBE, showHarvestLevel, "If true we show harvest level while the player is sneaking");
    }
}
