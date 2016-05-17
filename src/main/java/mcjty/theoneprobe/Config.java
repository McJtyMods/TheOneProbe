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
    public static boolean showDebugInfo = true;
    public static int leftX = 20;
    public static int topY = 20;
    public static int rightX = -1;
    public static int bottomY = -1;

    public static int boxBorderColor = 0;
    public static int boxFillColor = 0;
    public static int boxThickness = 0;

    public static void init(Configuration cfg) {
        needsProbe = cfg.getBoolean("needsProbe", CATEGORY_THEONEPROBE, needsProbe, "If true the probe is needed to show the tooltip. If false the tooltip shows all the time");
        showRF = cfg.getInt("showRF", CATEGORY_THEONEPROBE, showRF, 0, 2, "How to display RF: 0 = do not show, 1 = show in a bar, 2 = show as text");
        int fmt = cfg.getInt("rfFormat", CATEGORY_THEONEPROBE, rfFormat.ordinal(), 0, 2, "Format for displaying RF: 0 = full, 1 = compact, 2 = comma separated");
        rfFormat = NumberFormat.values()[fmt];
        timeout = cfg.getInt("timeout", CATEGORY_THEONEPROBE, timeout, 10, 100000, "The amount of milliseconds to wait before updating probe information from the server");
        showHarvestLevel = cfg.getBoolean("showHarvestLevel", CATEGORY_THEONEPROBE, showHarvestLevel, "If true show harvest level while the player is sneaking");
        showDebugInfo = cfg.getBoolean("showDebugInfo", CATEGORY_THEONEPROBE, showDebugInfo, "If true show debug info with creative probe");

        leftX = cfg.getInt("boxLeftX", CATEGORY_THEONEPROBE, leftX, -1, 10000, "The distance to the left side of the screen. Use -1 if you don't want to set this");
        rightX = cfg.getInt("boxRightX", CATEGORY_THEONEPROBE, rightX, -1, 10000, "The distance to the right side of the screen. Use -1 if you don't want to set this");
        topY = cfg.getInt("boxTopY", CATEGORY_THEONEPROBE, topY, -1, 10000, "The distance to the top side of the screen. Use -1 if you don't want to set this");
        bottomY = cfg.getInt("boxBottomY", CATEGORY_THEONEPROBE, bottomY, -1, 10000, "The distance to the bottom side of the screen. Use -1 if you don't want to set this");
        boxBorderColor = parseColor(cfg.getString("boxBorderColor", CATEGORY_THEONEPROBE, Integer.toHexString(boxBorderColor), "Color of the border of the box (0 to disable)"));
        boxFillColor = parseColor(cfg.getString("boxFillColor", CATEGORY_THEONEPROBE, Integer.toHexString(boxFillColor), "Color of the box (0 to disable)"));
        boxThickness = parseColor(cfg.getString("boxThickness", CATEGORY_THEONEPROBE, Integer.toHexString(boxThickness), "Thickness of the border of the box (0 to disable)"));
    }

    private static int parseColor(String col) {
        try {
            return (int) Long.parseLong(col, 16);
        } catch (NumberFormatException e) {
            System.out.println("Config.parseColor");
            return 0;
        }
    }
}
