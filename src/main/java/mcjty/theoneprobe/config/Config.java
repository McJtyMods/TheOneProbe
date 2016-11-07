package mcjty.theoneprobe.config;


import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.styles.DefaultOverlayStyle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import java.util.HashSet;
import java.util.Set;

public class Config {
    public static String CATEGORY_THEONEPROBE = "theoneprobe";
    public static String CATEGORY_PROVIDERS = "providers";
    public static String CATEGORY_CLIENT = "client";

    public static int MODE_NOT = 0;
    public static int MODE_NORMAL = 1;
    public static int MODE_EXTENDED = 2;

    public static int needsProbe = 1;
    public static boolean extendedInMain = false;
    public static NumberFormat rfFormat = NumberFormat.COMPACT;
    public static NumberFormat tankFormat = NumberFormat.COMPACT;
    public static int timeout = 200;
    public static int waitingForServerTimeout = 2000;

    public static boolean supportBaubles = true;

    // Chest related settings
    public static int showSmallChestContentsWithoutSneaking = 0;
    public static int showItemDetailThresshold = 4;
    public static String[] showContentsWithoutSneaking = { "storagedrawers:basicDrawers" };
    public static String[] dontShowContentsUnlessSneaking = {};
    private static Set<ResourceLocation> inventoriesToShow = null;
    private static Set<ResourceLocation> inventoriesToNotShow = null;

    public static float probeDistance = 6;
    public static boolean showLiquids = false;
    public static boolean isVisible = true;
    public static boolean compactEqualStacks = true;
    public static boolean holdKeyToMakeVisible = false;

    public static boolean showDebugInfo = true;

    private static int leftX = 5;
    private static int topY = 5;
    private static int rightX = -1;
    private static int bottomY = -1;

    public static int showBreakProgress = 1;    // 0 == off, 1 == bar, 2 == text
    public static boolean harvestStyleVanilla = true;

    public static int chestContentsBorderColor = 0xff006699;
    private static int boxBorderColor = 0xff999999;
    private static int boxFillColor = 0x55006699;
    private static int boxThickness = 2;

    public static float tooltipScale = 1.0f;

    public static int rfbarFilledColor = 0xffdd0000;
    public static int rfbarAlternateFilledColor = 0xff430000;
    public static int rfbarBorderColor = 0xff555555;

    public static int tankbarFilledColor = 0xff0000dd;
    public static int tankbarAlternateFilledColor = 0xff000043;
    public static int tankbarBorderColor = 0xff555555;

    public static int loggingThrowableTimeout = 20000;


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
        loggingThrowableTimeout = cfg.getInt("loggingThrowableTimeout", CATEGORY_THEONEPROBE, loggingThrowableTimeout, 1, 10000000, "How much time (ms) to wait before reporting an exception again");
        needsProbe = cfg.getInt("needsProbe", CATEGORY_THEONEPROBE, needsProbe, 0, 2, "Is the probe needed to show the tooltip? 0 = no, 1 = yes, 2 = yes and clients cannot override");
        extendedInMain = cfg.getBoolean("extendedInMain", CATEGORY_THEONEPROBE, extendedInMain, "If true the probe will automatically show extended information if it is in your main hand (so not required to sneak)");
        supportBaubles = cfg.getBoolean("supportBaubles", CATEGORY_THEONEPROBE, supportBaubles, "If true there will be a bauble version of the probe if baubles is present");
        defaultConfig.setRFMode(cfg.getInt("showRF", CATEGORY_THEONEPROBE, defaultConfig.getRFMode(), 0, 2, "How to display RF: 0 = do not show, 1 = show in a bar, 2 = show as text"));
        defaultConfig.setTankMode(cfg.getInt("showTank", CATEGORY_THEONEPROBE, defaultConfig.getTankMode(), 0, 2, "How to display tank contents: 0 = do not show, 1 = show in a bar, 2 = show as text"));
        int fmt = cfg.getInt("rfFormat", CATEGORY_THEONEPROBE, rfFormat.ordinal(), 0, 2, "Format for displaying RF: 0 = full, 1 = compact, 2 = comma separated");
        rfFormat = NumberFormat.values()[fmt];
        fmt = cfg.getInt("tankFormat", CATEGORY_THEONEPROBE, tankFormat.ordinal(), 0, 2, "Format for displaying tank contents: 0 = full, 1 = compact, 2 = comma separated");
        tankFormat = NumberFormat.values()[fmt];
        timeout = cfg.getInt("timeout", CATEGORY_THEONEPROBE, timeout, 10, 100000, "The amount of milliseconds to wait before updating probe information from the server");
        waitingForServerTimeout = cfg.getInt("waitingForServerTimeout", CATEGORY_THEONEPROBE, waitingForServerTimeout, -1, 100000, "The amount of milliseconds to wait before showing a 'fetch from server' info on the client (if the server is slow to respond) (-1 to disable this feature)");
        probeDistance = cfg.getFloat("probeDistance", CATEGORY_THEONEPROBE, probeDistance, 0.1f, 200f, "Distance at which the probe works");
        defaultConfig.showModName(IProbeConfig.ConfigMode.values()[cfg.getInt("showModName", CATEGORY_THEONEPROBE, defaultConfig.getShowModName().ordinal(), 0, 2, "Show mod name (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showHarvestLevel(IProbeConfig.ConfigMode.values()[cfg.getInt("showHarvestLevel", CATEGORY_THEONEPROBE, defaultConfig.getShowHarvestLevel().ordinal(), 0, 2, "Show harvest level (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showCanBeHarvested(IProbeConfig.ConfigMode.values()[cfg.getInt("showCanBeHarvested", CATEGORY_THEONEPROBE, defaultConfig.getShowHarvestLevel().ordinal(), 0, 2, "Show if the block can be harvested (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showCropPercentage(IProbeConfig.ConfigMode.values()[cfg.getInt("showCropPercentage", CATEGORY_THEONEPROBE, defaultConfig.getShowCropPercentage().ordinal(), 0, 2, "Show the growth level of crops (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showChestContents(IProbeConfig.ConfigMode.values()[cfg.getInt("showChestContents", CATEGORY_THEONEPROBE, defaultConfig.getShowChestContents().ordinal(), 0, 2, "Show chest contents (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showChestContentsDetailed(IProbeConfig.ConfigMode.values()[cfg.getInt("showChestContentsDetailed", CATEGORY_THEONEPROBE, defaultConfig.getShowChestContentsDetailed().ordinal(), 0, 2, "Show chest contents in detail (0 = not, 1 = always, 2 = sneak), used only if number of items is below 'showItemDetailThresshold'")]);
        defaultConfig.showRedstone(IProbeConfig.ConfigMode.values()[cfg.getInt("showRedstone", CATEGORY_THEONEPROBE, defaultConfig.getShowRedstone().ordinal(), 0, 2, "Show redstone (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showMobHealth(IProbeConfig.ConfigMode.values()[cfg.getInt("showMobHealth", CATEGORY_THEONEPROBE, defaultConfig.getShowMobHealth().ordinal(), 0, 2, "Show mob health (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showMobPotionEffects(IProbeConfig.ConfigMode.values()[cfg.getInt("showMobPotionEffects", CATEGORY_THEONEPROBE, defaultConfig.getShowMobPotionEffects().ordinal(), 0, 2, "Show mob potion effects (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showLeverSetting(IProbeConfig.ConfigMode.values()[cfg.getInt("showLeverSetting", CATEGORY_THEONEPROBE, defaultConfig.getShowLeverSetting().ordinal(), 0, 2, "Show lever/comparator/repeater settings (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showTankSetting(IProbeConfig.ConfigMode.values()[cfg.getInt("showTankSetting", CATEGORY_THEONEPROBE, defaultConfig.getShowTankSetting().ordinal(), 0, 2, "Show tank setting (0 = not, 1 = always, 2 = sneak)")]);
        showDebugInfo = cfg.getBoolean("showDebugInfo", CATEGORY_THEONEPROBE, showDebugInfo, "If true show debug info with creative probe");
        compactEqualStacks = cfg.getBoolean("compactEqualStacks", CATEGORY_THEONEPROBE, compactEqualStacks, "If true equal stacks will be compacted in the chest contents overlay");
        rfbarFilledColor = parseColor(cfg.getString("rfbarFilledColor", CATEGORY_THEONEPROBE, Integer.toHexString(rfbarFilledColor), "Color for the RF bar"));
        rfbarAlternateFilledColor = parseColor(cfg.getString("rfbarAlternateFilledColor", CATEGORY_THEONEPROBE, Integer.toHexString(rfbarAlternateFilledColor), "Alternate color for the RF bar"));
        rfbarBorderColor = parseColor(cfg.getString("rfbarBorderColor", CATEGORY_THEONEPROBE, Integer.toHexString(rfbarBorderColor), "Color for the RF bar border"));
        tankbarFilledColor = parseColor(cfg.getString("tankbarFilledColor", CATEGORY_THEONEPROBE, Integer.toHexString(tankbarFilledColor), "Color for the tank bar"));
        tankbarAlternateFilledColor = parseColor(cfg.getString("tankbarAlternateFilledColor", CATEGORY_THEONEPROBE, Integer.toHexString(tankbarAlternateFilledColor), "Alternate color for the tank bar"));
        tankbarBorderColor = parseColor(cfg.getString("tankbarBorderColor", CATEGORY_THEONEPROBE, Integer.toHexString(tankbarBorderColor), "Color for the tank bar border"));

        showItemDetailThresshold = cfg.getInt("showItemDetailThresshold", CATEGORY_THEONEPROBE, showItemDetailThresshold, 0, 20, "If the number of items in an inventory is lower or equal then this number then more info is shown");
        showSmallChestContentsWithoutSneaking = cfg.getInt("showSmallChestContentsWithoutSneaking", CATEGORY_THEONEPROBE, showSmallChestContentsWithoutSneaking, 0, 1000, "The maximum amount of slots (empty or not) to show without sneaking");
        showContentsWithoutSneaking = cfg.getStringList("showContentsWithoutSneaking", CATEGORY_THEONEPROBE, showContentsWithoutSneaking, "A list of blocks for which we automatically show chest contents even if not sneaking");
        dontShowContentsUnlessSneaking = cfg.getStringList("dontShowContentsUnlessSneaking", CATEGORY_THEONEPROBE, dontShowContentsUnlessSneaking, "A list of blocks for which we don't show chest contents automatically except if sneaking");

        setupStyleConfig(cfg);
    }

    public static void setupStyleConfig(Configuration cfg) {
        leftX = cfg.getInt("boxLeftX", CATEGORY_CLIENT, leftX, -1, 10000, "The distance to the left side of the screen. Use -1 if you don't want to set this");
        rightX = cfg.getInt("boxRightX", CATEGORY_CLIENT, rightX, -1, 10000, "The distance to the right side of the screen. Use -1 if you don't want to set this");
        topY = cfg.getInt("boxTopY", CATEGORY_CLIENT, topY, -1, 10000, "The distance to the top side of the screen. Use -1 if you don't want to set this");
        bottomY = cfg.getInt("boxBottomY", CATEGORY_CLIENT, bottomY, -1, 10000, "The distance to the bottom side of the screen. Use -1 if you don't want to set this");
        boxBorderColor = parseColor(cfg.getString("boxBorderColor", CATEGORY_CLIENT, Integer.toHexString(boxBorderColor), "Color of the border of the box (0 to disable)"));
        boxFillColor = parseColor(cfg.getString("boxFillColor", CATEGORY_CLIENT, Integer.toHexString(boxFillColor), "Color of the box (0 to disable)"));
        boxThickness = cfg.getInt("boxThickness", CATEGORY_CLIENT, boxThickness, 0, 20, "Thickness of the border of the box (0 to disable)");
        showLiquids = cfg.getBoolean("showLiquids", CATEGORY_CLIENT, showLiquids, "If true show liquid information when the probe hits liquid first");
        isVisible = cfg.getBoolean("isVisible", CATEGORY_CLIENT, isVisible, "Toggle default probe visibility (client can override)");
        holdKeyToMakeVisible = cfg.getBoolean("holdKeyToMakeVisible", CATEGORY_CLIENT, holdKeyToMakeVisible, "If true then the probe hotkey must be held down to show the tooltip");
        compactEqualStacks = cfg.getBoolean("compactEqualStacks", CATEGORY_CLIENT, compactEqualStacks, "If true equal stacks will be compacted in the chest contents overlay");
        tooltipScale = cfg.getFloat("tooltipScale", CATEGORY_CLIENT, tooltipScale, 0.4f, 5.0f, "The scale of the tooltips, 1 is default, 2 is smaller");
        chestContentsBorderColor = parseColor(cfg.getString("chestContentsBorderColor", CATEGORY_CLIENT, Integer.toHexString(chestContentsBorderColor), "Color of the border of the chest contents box (0 to disable)"));
        showBreakProgress = cfg.getInt("showBreakProgress", CATEGORY_CLIENT, showBreakProgress, 0, 2, "0 means don't show break progress, 1 is show as bar, 2 is show as text");
        harvestStyleVanilla = cfg.getBoolean("harvestStyleVanilla", CATEGORY_CLIENT, harvestStyleVanilla, "true means shows harvestability with vanilla style icons");

        extendedInMain = cfg.getBoolean("extendedInMain", CATEGORY_CLIENT, extendedInMain, "If true the probe will automatically show extended information if it is in your main hand (so not required to sneak)");
    }

    public static void setExtendedInMain(boolean extendedInMain) {
        Configuration cfg = TheOneProbe.config;
        Config.extendedInMain = extendedInMain;
        cfg.get(CATEGORY_CLIENT, "extendedInMain", extendedInMain).set(extendedInMain);
        cfg.save();
    }

    public static void setLiquids(boolean liquids) {
        Configuration cfg = TheOneProbe.config;
        Config.showLiquids = liquids;
        cfg.get(CATEGORY_CLIENT, "showLiquids", showLiquids).set(liquids);
        cfg.save();
    }

    public static void setVisible(boolean visible) {
        Configuration cfg = TheOneProbe.config;
        Config.isVisible = visible;
        cfg.get(CATEGORY_CLIENT, "isVisible", isVisible).set(visible);
        cfg.save();
    }

    public static void setCompactEqualStacks(boolean compact) {
        Configuration cfg = TheOneProbe.config;
        Config.compactEqualStacks = compact;
        cfg.get(CATEGORY_CLIENT, "compactEqualStacks", compactEqualStacks).set(compact);
        cfg.save();
    }

    public static void setPos(int leftx, int topy, int rightx, int bottomy) {
        Configuration cfg = TheOneProbe.config;
        Config.leftX = leftx;
        Config.topY = topy;
        Config.rightX = rightx;
        Config.bottomY = bottomy;
        cfg.get(CATEGORY_CLIENT, "boxLeftX", leftx).set(leftx);
        cfg.get(CATEGORY_CLIENT, "boxRightX", rightx).set(rightx);
        cfg.get(CATEGORY_CLIENT, "boxTopY", topy).set(topy);
        cfg.get(CATEGORY_CLIENT, "boxBottomY", bottomy).set(bottomy);
        cfg.save();
        updateDefaultOverlayStyle();
    }

    public static void setBoxStyle(int thickness, int borderColor, int fillcolor) {
        Configuration cfg = TheOneProbe.config;
        boxThickness = thickness;
        boxBorderColor = borderColor;
        boxFillColor = fillcolor;
        cfg.get(CATEGORY_CLIENT, "boxThickness", thickness).set(thickness);
        cfg.get(CATEGORY_CLIENT, "boxBorderColor", Integer.toHexString(borderColor)).set(Integer.toHexString(borderColor));
        cfg.get(CATEGORY_CLIENT, "boxFillColor", Integer.toHexString(fillcolor)).set(Integer.toHexString(fillcolor));
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

    public static void updateDefaultOverlayStyle() {
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

    public static Set<ResourceLocation> getInventoriesToShow() {
        if (inventoriesToShow == null) {
            inventoriesToShow = new HashSet<>();
            for (String s : showContentsWithoutSneaking) {
                inventoriesToShow.add(new ResourceLocation(s));
            }
        }
        return inventoriesToShow;
    }

    public static Set<ResourceLocation> getInventoriesToNotShow() {
        if (inventoriesToNotShow == null) {
            inventoriesToNotShow = new HashSet<>();
            for (String s : dontShowContentsUnlessSneaking) {
                inventoriesToNotShow.add(new ResourceLocation(s));
            }
        }
        return inventoriesToNotShow;
    }
}
