package mcjty.theoneprobe;


import mcjty.theoneprobe.api.NumberFormat;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    public static String CATEGORY_THEONEPROBE = "theoneprobe";
    public static String CATEGORY_PROVIDERS = "providers";

    public static boolean needsProbe = true;
    public static int showRF = 1;
    public static NumberFormat rfFormat = NumberFormat.COMPACT;
    public static int timeout = 200;
    public static boolean showHarvestLevel = true;
    public static boolean showCropPercentage = true;
    public static boolean showChestContents = true;
    public static boolean showDebugInfo = true;
    public static int leftX = 5;
    public static int topY = 5;
    public static int rightX = -1;
    public static int bottomY = -1;

    public static int boxBorderColor = 0xff999999;
    public static int boxFillColor = 0x55006699;
    public static int boxThickness = 2;

    public static int rfbarFilledColor = 0xffdd0000;
    public static int rfbarAlternateFilledColor = 0xff430000;
    public static int rfbarBorderColor = 0xff555555;

    public static void init(Configuration cfg) {
        needsProbe = cfg.getBoolean("needsProbe", CATEGORY_THEONEPROBE, needsProbe, "If true the probe is needed to show the tooltip. If false the tooltip shows all the time");
        showRF = cfg.getInt("showRF", CATEGORY_THEONEPROBE, showRF, 0, 2, "How to display RF: 0 = do not show, 1 = show in a bar, 2 = show as text");
        int fmt = cfg.getInt("rfFormat", CATEGORY_THEONEPROBE, rfFormat.ordinal(), 0, 2, "Format for displaying RF: 0 = full, 1 = compact, 2 = comma separated");
        rfFormat = NumberFormat.values()[fmt];
        timeout = cfg.getInt("timeout", CATEGORY_THEONEPROBE, timeout, 10, 100000, "The amount of milliseconds to wait before updating probe information from the server");
        showHarvestLevel = cfg.getBoolean("showHarvestLevel", CATEGORY_THEONEPROBE, showHarvestLevel, "If true show harvest level while the player is sneaking");
        showCropPercentage = cfg.getBoolean("showCropPercentage", CATEGORY_THEONEPROBE, showCropPercentage, "If true show the growth level of crops");
        showChestContents = cfg.getBoolean("showChestContents", CATEGORY_THEONEPROBE, showChestContents, "If true show chest contents while the player is sneaking");
        showDebugInfo = cfg.getBoolean("showDebugInfo", CATEGORY_THEONEPROBE, showDebugInfo, "If true show debug info with creative probe");

        setupStyleConfig(cfg);
    }

    public static Configuration initClientConfig() {
        Configuration cfg = new Configuration(new File(TheOneProbe.modConfigDir, "theoneprobe_client.cfg"));
        cfg.load();
        setupStyleConfig(cfg);
        return cfg;
    }

    private static void setupStyleConfig(Configuration cfg) {
        leftX = cfg.getInt("boxLeftX", CATEGORY_THEONEPROBE, leftX, -1, 10000, "The distance to the left side of the screen. Use -1 if you don't want to set this");
        rightX = cfg.getInt("boxRightX", CATEGORY_THEONEPROBE, rightX, -1, 10000, "The distance to the right side of the screen. Use -1 if you don't want to set this");
        topY = cfg.getInt("boxTopY", CATEGORY_THEONEPROBE, topY, -1, 10000, "The distance to the top side of the screen. Use -1 if you don't want to set this");
        bottomY = cfg.getInt("boxBottomY", CATEGORY_THEONEPROBE, bottomY, -1, 10000, "The distance to the bottom side of the screen. Use -1 if you don't want to set this");
        boxBorderColor = parseColor(cfg.getString("boxBorderColor", CATEGORY_THEONEPROBE, Integer.toHexString(boxBorderColor), "Color of the border of the box (0 to disable)"));
        boxFillColor = parseColor(cfg.getString("boxFillColor", CATEGORY_THEONEPROBE, Integer.toHexString(boxFillColor), "Color of the box (0 to disable)"));
        boxThickness = cfg.getInt("boxThickness", CATEGORY_THEONEPROBE, boxThickness, 0, 20, "Thickness of the border of the box (0 to disable)");
    }

    public static void setPos(int leftx, int topy, int rightx, int bottomy) {
        Configuration cfg = initClientConfig();
        Config.leftX = leftx;
        Config.topY = topy;
        Config.rightX = rightx;
        Config.bottomY = bottomy;
        cfg.get(CATEGORY_THEONEPROBE, "boxLeftX", leftx).set(leftx);
        cfg.get(CATEGORY_THEONEPROBE, "boxRightX", rightx).set(rightx);
        cfg.get(CATEGORY_THEONEPROBE, "boxTopY", topy).set(topy);
        cfg.get(CATEGORY_THEONEPROBE, "boxBottomY", bottomy).set(bottomy);
        cfg.save();
    }

    public static void setBoxStyle(int thickness, int borderColor, int fillcolor) {
        Configuration cfg = initClientConfig();
        boxThickness = thickness;
        boxBorderColor = borderColor;
        boxFillColor = fillcolor;
        cfg.get(CATEGORY_THEONEPROBE, "boxThickness", thickness).set(thickness);
        cfg.get(CATEGORY_THEONEPROBE, "boxBorderColor", Integer.toHexString(borderColor)).set(Integer.toHexString(borderColor));
        cfg.get(CATEGORY_THEONEPROBE, "boxFillColor", Integer.toHexString(fillcolor)).set(Integer.toHexString(fillcolor));
        cfg.save();
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
