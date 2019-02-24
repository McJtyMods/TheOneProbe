package mcjty.theoneprobe.config;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.styles.DefaultOverlayStyle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static mcjty.theoneprobe.api.TextStyleClass.*;
import static net.minecraftforge.fml.Logging.CORE;

@Mod.EventBusSubscriber
public class Config {

    private static final Builder SERVER_BUILDER = new Builder();
    private static final Builder CLIENT_BUILDER = new Builder();

    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final ForgeConfigSpec CLIENT_CONFIG;


    public static String CATEGORY_THEONEPROBE = "theoneprobe";
    public static String CATEGORY_PROVIDERS = "providers";
    public static String CATEGORY_CLIENT = "client";

    public static final int PROBE_NOTNEEDED = 0;
    public static final int PROBE_NEEDED = 1;
    public static final int PROBE_NEEDEDHARD = 2;
    public static final int PROBE_NEEDEDFOREXTENDED = 3;
    public static IntValue needsProbe;

    public static BooleanValue extendedInMain;
    public static NumberFormat rfFormat = NumberFormat.COMPACT;
    public static NumberFormat tankFormat = NumberFormat.COMPACT;
    public static int timeout = 300;
    public static int waitingForServerTimeout = 2000;
    public static int maxPacketToServer = 20000;

    public static BooleanValue supportBaubles;
    public static BooleanValue spawnNote;

    // Chest related settings
    public static int showSmallChestContentsWithoutSneaking = 0;
    public static int showItemDetailThresshold = 4;
    public static String[] showContentsWithoutSneaking = {"storagedrawers:basicDrawers", "storagedrawersextra:extra_drawers"};
    public static String[] dontShowContentsUnlessSneaking = {};
    public static String[] dontSendNBT = {};
    private static Set<ResourceLocation> inventoriesToShow = null;
    private static Set<ResourceLocation> inventoriesToNotShow = null;
    private static Set<ResourceLocation> dontSendNBTSet = null;

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
    private static int boxOffset = 0;

    public static float tooltipScale = 1.0f;

    public static int rfbarFilledColor = 0xffdd0000;
    public static int rfbarAlternateFilledColor = 0xff430000;
    public static int rfbarBorderColor = 0xff555555;

    public static int tankbarFilledColor = 0xff0000dd;
    public static int tankbarAlternateFilledColor = 0xff000043;
    public static int tankbarBorderColor = 0xff555555;

    public static Map<TextStyleClass, String> defaultTextStyleClasses = new HashMap<>();
    public static Map<TextStyleClass, String> textStyleClasses = new HashMap<>();

    static {
        defaultTextStyleClasses.put(NAME, "white");
        defaultTextStyleClasses.put(MODNAME, "blue,italic");
        defaultTextStyleClasses.put(ERROR, "red,bold");
        defaultTextStyleClasses.put(WARNING, "yellow");
        defaultTextStyleClasses.put(OK, "green");
        defaultTextStyleClasses.put(INFO, "white");
        defaultTextStyleClasses.put(INFOIMP, "blue");
        defaultTextStyleClasses.put(OBSOLETE, "gray,strikethrough");
        defaultTextStyleClasses.put(LABEL, "gray");
        defaultTextStyleClasses.put(PROGRESS, "white");
        textStyleClasses = new HashMap<>(defaultTextStyleClasses);
    }

    public static IntValue loggingThrowableTimeout;

    public static BooleanValue showCollarColor;

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


    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        TheOneProbe.logger.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        TheOneProbe.logger.debug("Built TOML config for {}", path.toString());
        configData.load();
        TheOneProbe.logger.debug("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);
    }


    static {
        SERVER_BUILDER.comment("General configuration");
        CLIENT_BUILDER.comment("General configuration");
        loggingThrowableTimeout = SERVER_BUILDER
                .comment("How much time (ms) to wait before reporting an exception again")
                .defineInRange("loggingThrowableTimeout", 20000, 1, 10000000);

        needsProbe = SERVER_BUILDER
                .comment("Is the probe needed to show the tooltip? 0 = no, 1 = yes, 2 = yes and clients cannot override, 3 = probe needed for extended info only")
                .defineInRange("needsProbe", PROBE_NEEDEDFOREXTENDED, 0, 3);

        extendedInMain = SERVER_BUILDER
                .comment("If true the probe will automatically show extended information if it is in your main hand (so not required to sneak)")
                .define("extendedInMain", false);
        supportBaubles = SERVER_BUILDER
                .comment("If true there will be a bauble version of the probe if baubles is present")
                .define("supportBaubles", true);
        spawnNote = SERVER_BUILDER
                .comment("If true there will be a readme note for first-time players")
                .define("spawnNote", true);
        showCollarColor = SERVER_BUILDER
                .comment("If true show the color of the collar of a wolf")
                .define("showCollarColor", true);

//        defaultConfig.setRFMode(cfg.getInt("showRF", CATEGORY_THEONEPROBE, defaultConfig.getRFMode(), 0, 2, "How to display RF: 0 = do not show, 1 = show in a bar, 2 = show as text"));
//        defaultConfig.setTankMode(cfg.getInt("showTank", CATEGORY_THEONEPROBE, defaultConfig.getTankMode(), 0, 2, "How to display tank contents: 0 = do not show, 1 = show in a bar, 2 = show as text"));
//        int fmt = cfg.getInt("rfFormat", CATEGORY_THEONEPROBE, rfFormat.ordinal(), 0, 2, "Format for displaying RF: 0 = full, 1 = compact, 2 = comma separated");
//        rfFormat = NumberFormat.values()[fmt];
//        fmt = cfg.getInt("tankFormat", CATEGORY_THEONEPROBE, tankFormat.ordinal(), 0, 2, "Format for displaying tank contents: 0 = full, 1 = compact, 2 = comma separated");
//        tankFormat = NumberFormat.values()[fmt];
//        timeout = cfg.getInt("timeout", CATEGORY_THEONEPROBE, timeout, 10, 100000, "The amount of milliseconds to wait before updating probe information from the server (this is a client-side config)");
//        waitingForServerTimeout = cfg.getInt("waitingForServerTimeout", CATEGORY_THEONEPROBE, waitingForServerTimeout, -1, 100000, "The amount of milliseconds to wait before showing a 'fetch from server' info on the client (if the server is slow to respond) (-1 to disable this feature)");
//        maxPacketToServer = cfg.getInt("maxPacketToServer", CATEGORY_THEONEPROBE, maxPacketToServer, -1, 32768, "The maximum packet size to send an itemstack from client to server. Reduce this if you have issues with network lag caused by TOP");
//        probeDistance = cfg.getFloat("probeDistance", CATEGORY_THEONEPROBE, probeDistance, 0.1f, 200f, "Distance at which the probe works");
//        initDefaultConfig(cfg);
//
//        showDebugInfo = cfg.getBoolean("showDebugInfo", CATEGORY_THEONEPROBE, showDebugInfo, "If true show debug info with creative probe");
//        compactEqualStacks = cfg.getBoolean("compactEqualStacks", CATEGORY_THEONEPROBE, compactEqualStacks, "If true equal stacks will be compacted in the chest contents overlay");
//        rfbarFilledColor = parseColor(cfg.getString("rfbarFilledColor", CATEGORY_THEONEPROBE, Integer.toHexString(rfbarFilledColor), "Color for the RF bar"));
//        rfbarAlternateFilledColor = parseColor(cfg.getString("rfbarAlternateFilledColor", CATEGORY_THEONEPROBE, Integer.toHexString(rfbarAlternateFilledColor), "Alternate color for the RF bar"));
//        rfbarBorderColor = parseColor(cfg.getString("rfbarBorderColor", CATEGORY_THEONEPROBE, Integer.toHexString(rfbarBorderColor), "Color for the RF bar border"));
//        tankbarFilledColor = parseColor(cfg.getString("tankbarFilledColor", CATEGORY_THEONEPROBE, Integer.toHexString(tankbarFilledColor), "Color for the tank bar"));
//        tankbarAlternateFilledColor = parseColor(cfg.getString("tankbarAlternateFilledColor", CATEGORY_THEONEPROBE, Integer.toHexString(tankbarAlternateFilledColor), "Alternate color for the tank bar"));
//        tankbarBorderColor = parseColor(cfg.getString("tankbarBorderColor", CATEGORY_THEONEPROBE, Integer.toHexString(tankbarBorderColor), "Color for the tank bar border"));
//
//        showItemDetailThresshold = cfg.getInt("showItemDetailThresshold", CATEGORY_THEONEPROBE, showItemDetailThresshold, 0, 20, "If the number of items in an inventory is lower or equal then this number then more info is shown");
//        showSmallChestContentsWithoutSneaking = cfg.getInt("showSmallChestContentsWithoutSneaking", CATEGORY_THEONEPROBE, showSmallChestContentsWithoutSneaking, 0, 1000, "The maximum amount of slots (empty or not) to show without sneaking");
//        showContentsWithoutSneaking = cfg.getStringList("showContentsWithoutSneaking", CATEGORY_THEONEPROBE, showContentsWithoutSneaking, "A list of blocks for which we automatically show chest contents even if not sneaking");
//        dontShowContentsUnlessSneaking = cfg.getStringList("dontShowContentsUnlessSneaking", CATEGORY_THEONEPROBE, dontShowContentsUnlessSneaking, "A list of blocks for which we don't show chest contents automatically except if sneaking");
//        dontSendNBT = cfg.getStringList("dontSendNBT", CATEGORY_THEONEPROBE, dontSendNBT, "A list of blocks for which we don't send NBT over the network. This is mostly useful for blocks that have HUGE NBT in their pickblock (itemstack)");
//
//        setupStyleConfig(cfg);
        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void initDefaultConfig(Configuration cfg) {
        defaultConfig.showModName(IProbeConfig.ConfigMode.values()[cfg.getInt("showModName", CATEGORY_THEONEPROBE, defaultConfig.getShowModName().ordinal(), 0, 2, "Show mod name (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showHarvestLevel(IProbeConfig.ConfigMode.values()[cfg.getInt("showHarvestLevel", CATEGORY_THEONEPROBE, defaultConfig.getShowHarvestLevel().ordinal(), 0, 2, "Show harvest level (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showCanBeHarvested(IProbeConfig.ConfigMode.values()[cfg.getInt("showCanBeHarvested", CATEGORY_THEONEPROBE, defaultConfig.getShowHarvestLevel().ordinal(), 0, 2, "Show if the block can be harvested (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showCropPercentage(IProbeConfig.ConfigMode.values()[cfg.getInt("showCropPercentage", CATEGORY_THEONEPROBE, defaultConfig.getShowCropPercentage().ordinal(), 0, 2, "Show the growth level of crops (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showChestContents(IProbeConfig.ConfigMode.values()[cfg.getInt("showChestContents", CATEGORY_THEONEPROBE, defaultConfig.getShowChestContents().ordinal(), 0, 2, "Show chest contents (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showChestContentsDetailed(IProbeConfig.ConfigMode.values()[cfg.getInt("showChestContentsDetailed", CATEGORY_THEONEPROBE, defaultConfig.getShowChestContentsDetailed().ordinal(), 0, 2, "Show chest contents in detail (0 = not, 1 = always, 2 = sneak), used only if number of items is below 'showItemDetailThresshold'")]);
        defaultConfig.showRedstone(IProbeConfig.ConfigMode.values()[cfg.getInt("showRedstone", CATEGORY_THEONEPROBE, defaultConfig.getShowRedstone().ordinal(), 0, 2, "Show redstone (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showMobHealth(IProbeConfig.ConfigMode.values()[cfg.getInt("showMobHealth", CATEGORY_THEONEPROBE, defaultConfig.getShowMobHealth().ordinal(), 0, 2, "Show mob health (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showMobGrowth(IProbeConfig.ConfigMode.values()[cfg.getInt("showMobGrowth", CATEGORY_THEONEPROBE, defaultConfig.getShowMobGrowth().ordinal(), 0, 2, "Show time to adulthood for baby mobs (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showMobPotionEffects(IProbeConfig.ConfigMode.values()[cfg.getInt("showMobPotionEffects", CATEGORY_THEONEPROBE, defaultConfig.getShowMobPotionEffects().ordinal(), 0, 2, "Show mob potion effects (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showLeverSetting(IProbeConfig.ConfigMode.values()[cfg.getInt("showLeverSetting", CATEGORY_THEONEPROBE, defaultConfig.getShowLeverSetting().ordinal(), 0, 2, "Show lever/comparator/repeater settings (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showTankSetting(IProbeConfig.ConfigMode.values()[cfg.getInt("showTankSetting", CATEGORY_THEONEPROBE, defaultConfig.getShowTankSetting().ordinal(), 0, 2, "Show tank setting (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showBrewStandSetting(IProbeConfig.ConfigMode.values()[cfg.getInt("showBrewStandSetting", CATEGORY_THEONEPROBE, defaultConfig.getShowBrewStandSetting().ordinal(), 0, 2, "Show brewing stand setting (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showMobSpawnerSetting(IProbeConfig.ConfigMode.values()[cfg.getInt("showMobSpawnerSetting", CATEGORY_THEONEPROBE, defaultConfig.getShowMobSpawnerSetting().ordinal(), 0, 2, "Show mob spawner setting (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showAnimalOwnerSetting(IProbeConfig.ConfigMode.values()[cfg.getInt("showAnimalOwnerSetting", CATEGORY_THEONEPROBE, defaultConfig.getAnimalOwnerSetting().ordinal(), 0, 2, "Show animal owner setting (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showHorseStatSetting(IProbeConfig.ConfigMode.values()[cfg.getInt("showHorseStatSetting", CATEGORY_THEONEPROBE, defaultConfig.getHorseStatSetting().ordinal(), 0, 2, "Show horse stats setting (0 = not, 1 = always, 2 = sneak)")]);
        defaultConfig.showSilverfish(IProbeConfig.ConfigMode.values()[cfg.getInt("showSilverfish", CATEGORY_THEONEPROBE, defaultConfig.getShowSilverfish().ordinal(), 0, 2, "Reveal monster eggs (0 = not, 1 = always, 2 = sneak)")]);

    }

    public static void setProbeNeeded(int probeNeeded) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
//        Config.needsProbe = probeNeeded;
//        cfg.get(CATEGORY_THEONEPROBE, "needsProbe", probeNeeded).set(probeNeeded);
//        cfg.save();
    }


    public static void setupStyleConfig(Configuration cfg) {
        leftX = cfg.getInt("boxLeftX", CATEGORY_CLIENT, leftX, -1, 10000, "The distance to the left side of the screen. Use -1 if you don't want to set this");
        rightX = cfg.getInt("boxRightX", CATEGORY_CLIENT, rightX, -1, 10000, "The distance to the right side of the screen. Use -1 if you don't want to set this");
        topY = cfg.getInt("boxTopY", CATEGORY_CLIENT, topY, -1, 10000, "The distance to the top side of the screen. Use -1 if you don't want to set this");
        bottomY = cfg.getInt("boxBottomY", CATEGORY_CLIENT, bottomY, -1, 10000, "The distance to the bottom side of the screen. Use -1 if you don't want to set this");
        boxBorderColor = parseColor(cfg.getString("boxBorderColor", CATEGORY_CLIENT, Integer.toHexString(boxBorderColor), "Color of the border of the box (0 to disable)"));
        boxFillColor = parseColor(cfg.getString("boxFillColor", CATEGORY_CLIENT, Integer.toHexString(boxFillColor), "Color of the box (0 to disable)"));
        boxThickness = cfg.getInt("boxThickness", CATEGORY_CLIENT, boxThickness, 0, 20, "Thickness of the border of the box (0 to disable)");
        boxOffset = cfg.getInt("boxOffset", CATEGORY_CLIENT, boxOffset, 0, 20, "How much the border should be offset (i.e. to create an 'outer' border)");
        showLiquids = cfg.getBoolean("showLiquids", CATEGORY_CLIENT, showLiquids, "If true show liquid information when the probe hits liquid first");
        isVisible = cfg.getBoolean("isVisible", CATEGORY_CLIENT, isVisible, "Toggle default probe visibility (client can override)");
        holdKeyToMakeVisible = cfg.getBoolean("holdKeyToMakeVisible", CATEGORY_CLIENT, holdKeyToMakeVisible, "If true then the probe hotkey must be held down to show the tooltip");
        compactEqualStacks = cfg.getBoolean("compactEqualStacks", CATEGORY_CLIENT, compactEqualStacks, "If true equal stacks will be compacted in the chest contents overlay");
        tooltipScale = cfg.getFloat("tooltipScale", CATEGORY_CLIENT, tooltipScale, 0.4f, 5.0f, "The scale of the tooltips, 1 is default, 2 is smaller");
        chestContentsBorderColor = parseColor(cfg.getString("chestContentsBorderColor", CATEGORY_CLIENT, Integer.toHexString(chestContentsBorderColor), "Color of the border of the chest contents box (0 to disable)"));
        showBreakProgress = cfg.getInt("showBreakProgress", CATEGORY_CLIENT, showBreakProgress, 0, 2, "0 means don't show break progress, 1 is show as bar, 2 is show as text");
        harvestStyleVanilla = cfg.getBoolean("harvestStyleVanilla", CATEGORY_CLIENT, harvestStyleVanilla, "true means shows harvestability with vanilla style icons");

        Map<TextStyleClass, String> newformat = new HashMap<>();
        for (TextStyleClass styleClass : textStyleClasses.keySet()) {
            String style = cfg.getString("textStyle" + styleClass.getReadableName(),
                    CATEGORY_CLIENT, textStyleClasses.get(styleClass),
                    "Text style. Use a comma delimited list with colors like: 'red', 'green', 'blue', ... or style codes like 'underline', 'bold', 'italic', 'strikethrough', ...");
            newformat.put(styleClass, style);
        }
        textStyleClasses = newformat;

        // @todo 1.13
//        extendedInMain = cfg.getBoolean("extendedInMain", CATEGORY_CLIENT, extendedInMain, "If true the probe will automatically show extended information if it is in your main hand (so not required to sneak)");
    }

    public static void setTextStyle(TextStyleClass styleClass, String style) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
        Config.textStyleClasses.put(styleClass, style);
//        cfg.get(CATEGORY_CLIENT, "textStyle" + styleClass.getReadableName(), style).set(style);
//        cfg.save();
    }

    public static void setExtendedInMain(boolean extendedInMain) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
//        Config.extendedInMain = extendedInMain;
//        cfg.get(CATEGORY_CLIENT, "extendedInMain", extendedInMain).set(extendedInMain);
//        cfg.save();
    }

    public static void setLiquids(boolean liquids) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
        Config.showLiquids = liquids;
//        cfg.get(CATEGORY_CLIENT, "showLiquids", showLiquids).set(liquids);
//        cfg.save();
    }

    public static void setVisible(boolean visible) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
        Config.isVisible = visible;
//        cfg.get(CATEGORY_CLIENT, "isVisible", isVisible).set(visible);
//        cfg.save();
    }

    public static void setCompactEqualStacks(boolean compact) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
        Config.compactEqualStacks = compact;
//        cfg.get(CATEGORY_CLIENT, "compactEqualStacks", compactEqualStacks).set(compact);
//        cfg.save();
    }

    public static void setPos(int leftx, int topy, int rightx, int bottomy) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
        Config.leftX = leftx;
        Config.topY = topy;
        Config.rightX = rightx;
        Config.bottomY = bottomy;
//        cfg.get(CATEGORY_CLIENT, "boxLeftX", leftx).set(leftx);
//        cfg.get(CATEGORY_CLIENT, "boxRightX", rightx).set(rightx);
//        cfg.get(CATEGORY_CLIENT, "boxTopY", topy).set(topy);
//        cfg.get(CATEGORY_CLIENT, "boxBottomY", bottomy).set(bottomy);
//        cfg.save();
        updateDefaultOverlayStyle();
    }

    public static void setScale(float scale) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
        tooltipScale = scale;
//        cfg.get(CATEGORY_CLIENT, "tooltipScale", tooltipScale).set(tooltipScale);
//        cfg.save();
        updateDefaultOverlayStyle();
    }

    public static void setBoxStyle(int thickness, int borderColor, int fillcolor, int offset) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
        boxThickness = thickness;
        boxBorderColor = borderColor;
        boxFillColor = fillcolor;
        boxOffset = offset;
//        cfg.get(CATEGORY_CLIENT, "boxThickness", thickness).set(thickness);
//        cfg.get(CATEGORY_CLIENT, "boxBorderColor", Integer.toHexString(borderColor)).set(Integer.toHexString(borderColor));
//        cfg.get(CATEGORY_CLIENT, "boxFillColor", Integer.toHexString(fillcolor)).set(Integer.toHexString(fillcolor));
//        cfg.get(CATEGORY_CLIENT, "boxOffset", offset).set(offset);
//        cfg.save();
        updateDefaultOverlayStyle();
    }

    private static String configToTextFormat(String input) {
        if ("context".equals(input)) {
            return "context";
        }
        StringBuilder builder = new StringBuilder();
        String[] splitted = StringUtils.split(input, ',');
        for (String s : splitted) {
            TextFormatting format = TextFormatting.getValueByName(s);
            if (format != null) {
                builder.append(format.toString());
            }
        }
        return builder.toString();
    }

    public static String getTextStyle(TextStyleClass styleClass) {
        if (textStyleClasses.containsKey(styleClass)) {
            return configToTextFormat(textStyleClasses.get(styleClass));
        }
        return "";
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
                .borderOffset(boxOffset)
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

    public static Set<ResourceLocation> getDontSendNBTSet() {
        if (dontSendNBTSet == null) {
            dontSendNBTSet = new HashSet<>();
            for (String s : dontSendNBT) {
                dontSendNBTSet.add(new ResourceLocation(s));
            }
        }
        return dontSendNBTSet;
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
//        BLACKLIST.parseBlacklists();
        TheOneProbe.logger.debug("Loaded {} config file {}", TheOneProbe.MODID, configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
        TheOneProbe.logger.fatal(CORE, "{} config just got changed on the file system!", TheOneProbe.MODID);
    }
}
