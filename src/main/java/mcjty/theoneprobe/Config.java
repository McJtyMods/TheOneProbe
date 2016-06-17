package mcjty.theoneprobe;


import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.styles.DefaultOverlayStyle;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    public static String CATEGORY_THEONEPROBE = "theoneprobe";
    public static String CATEGORY_PROVIDERS = "providers";

    public static int MODE_NOT = 0;
    public static int MODE_NORMAL = 1;
    public static int MODE_EXTENDED = 2;

    public static int needsProbe = 1;
    public static boolean extendedInMain = false;
    public static NumberFormat rfFormat = NumberFormat.COMPACT;
    public static int timeout = 200;

    public static float probeDistance = 6;
    public static boolean showLiquids = false;
    public static boolean isVisible = true;

    public static boolean showDebugInfo = true;
    private static int leftX = 5;
    private static int topY = 5;
    private static int rightX = -1;
    private static int bottomY = -1;

    private static int boxBorderColor = 0xff999999;
    private static int boxFillColor = 0x55006699;
    private static int boxThickness = 2;

    public static int rfbarFilledColor = 0xffdd0000;
    public static int rfbarAlternateFilledColor = 0xff430000;
    public static int rfbarBorderColor = 0xff555555;


    private static IOverlayStyle defaultOverlayStyle;
    private static ProbeConfig defaultConfig = new ProbeConfig();
    private static IProbeConfig realConfig;

    public static ProbeConfig getDefaultConfig() {
        return defaultConfig;
    }

    public static void setRealConfig(IProbeConfig config) {
        realConfig = config;
    }

    public static IProbeConfig getRealConfig() {
        return realConfig;
    }

    public static void init(Configuration cfg) {
        needsProbe = cfg.getInt("needsProbe", CATEGORY_THEONEPROBE, needsProbe, 0, 2, "Is the probe needed to show the tooltip? 0 = no, 1 = yes, 2 = yes and clients cannot override");
        extendedInMain = cfg.getBoolean("extendedInMain", CATEGORY_THEONEPROBE, extendedInMain, "If true the probe will automatically show extended information if it is in your main hand (so not required to sneak)");
        defaultConfig.setRFMode(cfg.getInt("showRF", CATEGORY_THEONEPROBE, defaultConfig.getRFMode(), 0, 2, "How to display RF: 0 = do not show, 1 = show in a bar, 2 = show as text"));
        int fmt = cfg.getInt("rfFormat", CATEGORY_THEONEPROBE, rfFormat.ordinal(), 0, 2, "Format for displaying RF: 0 = full, 1 = compact, 2 = comma separated");
        rfFormat = NumberFormat.values()[fmt];
        timeout = cfg.getInt("timeout", CATEGORY_THEONEPROBE, timeout, 10, 100000, "The amount of milliseconds to wait before updating probe information from the server");
        probeDistance = cfg.getFloat("probeDistance", CATEGORY_THEONEPROBE, probeDistance, 0.1f, 200f, "Distance at which the probe works");
        defaultConfig.showModName(IProbeConfig.ConfigMode.values()[cfg.getInt("showModName", CATEGORY_THEONEPROBE, defaultConfig.getShowModName().ordinal(), 0, 2, "Show mod name (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showHarvestLevel(IProbeConfig.ConfigMode.values()[cfg.getInt("showHarvestLevel", CATEGORY_THEONEPROBE, defaultConfig.getShowHarvestLevel().ordinal(), 0, 2, "Show harvest level (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showCanBeHarvested(IProbeConfig.ConfigMode.values()[cfg.getInt("showCanBeHarvested", CATEGORY_THEONEPROBE, defaultConfig.getShowHarvestLevel().ordinal(), 0, 2, "Show if the block can be harvested (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showCropPercentage(IProbeConfig.ConfigMode.values()[cfg.getInt("showCropPercentage", CATEGORY_THEONEPROBE, defaultConfig.getShowCropPercentage().ordinal(), 0, 2, "Show the growth level of crops (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showChestContents(IProbeConfig.ConfigMode.values()[cfg.getInt("showChestContents", CATEGORY_THEONEPROBE, defaultConfig.getShowChestContents().ordinal(), 0, 2, "Show chest contents (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showRedstone(IProbeConfig.ConfigMode.values()[cfg.getInt("showRedstone", CATEGORY_THEONEPROBE, defaultConfig.getShowRedstone().ordinal(), 0, 2, "Show redstone (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showMobHealth(IProbeConfig.ConfigMode.values()[cfg.getInt("showMobHealth", CATEGORY_THEONEPROBE, defaultConfig.getShowMobHealth().ordinal(), 0, 2, "Show mob health (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showMobPotionEffects(IProbeConfig.ConfigMode.values()[cfg.getInt("showMobPotionEffects", CATEGORY_THEONEPROBE, defaultConfig.getShowMobPotionEffects().ordinal(), 0, 2, "Show mob potion effects (0 = not, 1 = always, 2 = sneak)")]);
        showDebugInfo = cfg.getBoolean("showDebugInfo", CATEGORY_THEONEPROBE, showDebugInfo, "If true show debug info with creative probe");
        showLiquids = cfg.getBoolean("showLiquids", CATEGORY_THEONEPROBE, showLiquids, "If true show liquid information when the probe hits liquid first");
        isVisible = cfg.getBoolean("isVisible", CATEGORY_THEONEPROBE, isVisible, "Toggle default probe visibility (client can override)");

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
        showLiquids = cfg.getBoolean("showLiquids", CATEGORY_THEONEPROBE, showLiquids, "If true show liquid information when the probe hits liquid first");
        isVisible = cfg.getBoolean("isVisible", CATEGORY_THEONEPROBE, isVisible, "Toggle default probe visibility (client can override)");
        extendedInMain = cfg.getBoolean("extendedInMain", CATEGORY_THEONEPROBE, extendedInMain, "If true the probe will automatically show extended information if it is in your main hand (so not required to sneak)");
    }

    public static void setExtendedInMain(boolean extendedInMain) {
        Configuration cfg = initClientConfig();
        Config.extendedInMain = extendedInMain;
        cfg.get(CATEGORY_THEONEPROBE, "extendedInMain", extendedInMain).set(extendedInMain);
        cfg.save();
    }

    public static void setLiquids(boolean liquids) {
        Configuration cfg = initClientConfig();
        Config.showLiquids = liquids;
        cfg.get(CATEGORY_THEONEPROBE, "showLiquids", showLiquids).set(liquids);
        cfg.save();
    }

    public static void setVisible(boolean visible) {
        Configuration cfg = initClientConfig();
        Config.isVisible = visible;
        cfg.get(CATEGORY_THEONEPROBE, "isVisible", isVisible).set(visible);
        cfg.save();
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
        updateDefaultOverlayStyle();
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
        updateDefaultOverlayStyle();
    }

    private static int parseColor(String col) {
        try {
            return (int) Long.parseLong(col, 16);
        } catch (NumberFormatException e) {
            System.out.println("Config.parseColor");
            return 0;
        }
    }

    private static void updateDefaultOverlayStyle() {
        defaultOverlayStyle = new DefaultOverlayStyle()
                .borderThickness(boxThickness)
                .borderColor(boxBorderColor)
                .boxColor(boxFillColor)
                .location(leftX, rightX, topY, bottomY);
    }

    public static IOverlayStyle getDefaultOverlayStyle() {
        if (defaultOverlayStyle == null) {
            updateDefaultOverlayStyle();
        }
        return defaultOverlayStyle;
    }

}
