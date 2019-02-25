package mcjty.theoneprobe.config;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
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
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.*;

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
    public static ConfigValue<NumberFormat> rfFormat;
    public static ConfigValue<NumberFormat> tankFormat;
    public static IntValue timeout;                 // Client-side
    public static IntValue waitingForServerTimeout; // Client-side
    public static IntValue maxPacketToServer;       // Client-side

    private static IntValue defaultRFMode;
    private static IntValue defaultTankMode;

    public static BooleanValue supportBaubles;
    public static BooleanValue spawnNote;

    // Chest related settings
    public static IntValue showSmallChestContentsWithoutSneaking;
    public static IntValue showItemDetailThresshold;
    private static ConfigValue<List<String>> showContentsWithoutSneaking;
    private static ConfigValue<List<String>> dontShowContentsUnlessSneaking;
    private static ConfigValue<List<String>> dontSendNBT;
    private static Set<ResourceLocation> inventoriesToShow = null;
    private static Set<ResourceLocation> inventoriesToNotShow = null;
    private static Set<ResourceLocation> dontSendNBTSet = null;

    public static DoubleValue probeDistance;        // Client-side
    public static BooleanValue showLiquids;
    public static BooleanValue isVisible;
    public static BooleanValue compactEqualStacks;
    public static BooleanValue holdKeyToMakeVisible;

    public static BooleanValue showDebugInfo;

    private static IntValue leftX;
    private static IntValue topY;
    private static IntValue rightX;
    private static IntValue bottomY;

    public static IntValue showBreakProgress;
    public static BooleanValue harvestStyleVanilla;

    private static ConfigValue<String> cfgchestContentsBorderColor;
    public static int chestContentsBorderColor = 0xff006699;

    private static ConfigValue<String> cfgboxBorderColor;
    private static ConfigValue<String> cfgboxFillColor;
    private static int boxBorderColor = 0xff999999;
    private static int boxFillColor = 0x55006699;

    private static IntValue boxThickness;
    private static IntValue boxOffset;

    public static DoubleValue tooltipScale;

    private static ConfigValue<String> cfgRfbarFilledColor;
    private static ConfigValue<String> cfgRfbarAlternateFilledColor;
    private static ConfigValue<String> cfgRfbarBorderColor;
    private static ConfigValue<String> cfgTankbarFilledColor;
    private static ConfigValue<String> cfgTankbarAlternateFilledColor;
    private static ConfigValue<String> cfgTankbarBorderColor;
    public static int rfbarFilledColor = 0xffdd0000;
    public static int rfbarAlternateFilledColor = 0xff430000;
    public static int rfbarBorderColor = 0xff555555;
    public static int tankbarFilledColor = 0xff0000dd;
    public static int tankbarAlternateFilledColor = 0xff000043;
    public static int tankbarBorderColor = 0xff555555;


    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowModName;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowHarvestLevel;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowCanBeHarvested;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowCropPercentage;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowChestContents;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowChestContentsDetailed;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowRedstone;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowMobHealth;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowMobGrowth;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowMobPotionEffects;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowLeverSetting;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowTankSetting;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowBrewStandSetting;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowMobSpawnerSetting;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowAnimalOwnerSetting;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowHorseStatSetting;
    private static ConfigValue<IProbeConfig.ConfigMode> cfgshowSilverfish;



    public static final Map<TextStyleClass, String> defaultTextStyleClasses = new HashMap<>();
    public static Map<TextStyleClass, ConfigValue<String>> cfgtextStyleClasses = new HashMap<>();
    public static Map<TextStyleClass, String> textStyleClasses = new HashMap<>();

    public static IntValue loggingThrowableTimeout;

    public static BooleanValue showCollarColor;

    private static IOverlayStyle defaultOverlayStyle;
    private static final ProbeConfig DEFAULT_CONFIG;
    private static IProbeConfig realConfig;

    public static ProbeConfig getDefaultConfig() {
        return DEFAULT_CONFIG;
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

        DEFAULT_CONFIG = new ProbeConfig();

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

        defaultRFMode = SERVER_BUILDER
                .comment("How to display RF: 0 = do not show, 1 = show in a bar, 2 = show as text")
                .defineInRange("showRF", DEFAULT_CONFIG.getRFMode(), 0, 2);
        defaultTankMode = SERVER_BUILDER
                .comment("How to display tank contents: 0 = do not show, 1 = show in a bar, 2 = show as text")
                .defineInRange("showTank", DEFAULT_CONFIG.getRFMode(), 0, 2);
        rfFormat = SERVER_BUILDER
                .comment("Format for displaying RF")
                .defineEnum("rfFormat", NumberFormat.COMPACT, NumberFormat.COMMAS, NumberFormat.COMPACT, NumberFormat.FULL, NumberFormat.NONE);
        tankFormat = SERVER_BUILDER
                .comment("Format for displaying tank contents")
                .defineEnum("tankFormat", NumberFormat.COMPACT, NumberFormat.COMMAS, NumberFormat.COMPACT, NumberFormat.FULL, NumberFormat.NONE);

        timeout = CLIENT_BUILDER
                .comment("The amount of milliseconds to wait before updating probe information from the server")
                .defineInRange("timeout", 300, 10, 100000);
        waitingForServerTimeout = CLIENT_BUILDER
                .comment("The amount of milliseconds to wait before showing a 'fetch from server' info on the client (if the server is slow to respond) (-1 to disable this feature)")
                .defineInRange("waitingForServerTimeout", 2000, -1, 100000);
        maxPacketToServer = CLIENT_BUILDER
                .comment("The maximum packet size to send an itemstack from client to server. Reduce this if you have issues with network lag caused by TOP")
                .defineInRange("maxPacketToServer", 20000, -1, 32768);
        probeDistance = CLIENT_BUILDER
                .comment("Distance at which the probe works")
                .defineInRange("probeDistance", 6.0, 0.1, 200);
        initDefaultConfig();

        showDebugInfo = SERVER_BUILDER
                .comment("If true show debug info with creative probe")
                .define("showDebugInfo", true);
        compactEqualStacks = SERVER_BUILDER
                .comment("If true equal stacks will be compacted in the chest contents overlay")
                .define("compactEqualStacks", true);

        cfgRfbarFilledColor = SERVER_BUILDER
                .comment("Color for the RF bar")
                .define("rfbarFilledColor", Integer.toHexString(rfbarFilledColor));
        cfgRfbarAlternateFilledColor = SERVER_BUILDER
                .comment("Alternate color for the RF bar")
                .define("rfbarAlternateFilledColor", Integer.toHexString(rfbarAlternateFilledColor));
        cfgRfbarBorderColor = SERVER_BUILDER
                .comment("Color for the RF bar border")
                .define("rfbarBorderColor", Integer.toHexString(rfbarBorderColor));
        cfgTankbarFilledColor = SERVER_BUILDER
                .comment("Color for the tank bar")
                .define("tankbarFilledColor", Integer.toHexString(tankbarFilledColor));
        cfgTankbarAlternateFilledColor = SERVER_BUILDER
                .comment("Alternate color for the tank bar")
                .define("tankbarAlternateFilledColor", Integer.toHexString(tankbarAlternateFilledColor));
        cfgTankbarBorderColor = SERVER_BUILDER
                .comment("Color for the tank bar border")
                .define("tankbarBorderColor", Integer.toHexString(tankbarBorderColor));

        showItemDetailThresshold = SERVER_BUILDER
            .comment("If the number of items in an inventory is lower or equal then this number then more info is shown")
            .defineInRange("showItemDetailThresshold", 4, 0, 20);
        showSmallChestContentsWithoutSneaking = SERVER_BUILDER
                .comment("The maximum amount of slots (empty or not) to show without sneaking")
                .defineInRange("showSmallChestContentsWithoutSneaking", 0, 0, 1000);
        showContentsWithoutSneaking = SERVER_BUILDER
                .comment("A list of blocks for which we automatically show chest contents even if not sneaking")
                .define("showContentsWithoutSneaking", Lists.<String>asList("storagedrawers:basicDrawers", new String[] { "storagedrawersextra:extra_drawers" }));
        dontShowContentsUnlessSneaking = SERVER_BUILDER
                .comment("A list of blocks for which we don't show chest contents automatically except if sneaking")
                .define("dontShowContentsUnlessSneaking", Collections.emptyList());

        dontSendNBT = SERVER_BUILDER
                .comment("A list of blocks for which we don't send NBT over the network. This is mostly useful for blocks that have HUGE NBT in their pickblock (itemstack)")
                .define("dontSendNBT", Collections.emptyList());

        setupStyleConfig();

        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static ConfigValue<IProbeConfig.ConfigMode> addModeConfig(String path, String comment, IProbeConfig.ConfigMode def) {
        return CLIENT_BUILDER
                .comment(comment)
                .defineEnum(path, def, IProbeConfig.ConfigMode.NORMAL, IProbeConfig.ConfigMode.EXTENDED, IProbeConfig.ConfigMode.NOT);
    }

    private static void initDefaultConfig() {
        cfgshowModName = addModeConfig("showModName", "Show mod name (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowModName());
        cfgshowHarvestLevel = addModeConfig("showHarvestLevel", "Show harvest level (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowHarvestLevel());
        cfgshowCanBeHarvested = addModeConfig("showCanBeHarvested", "Show if the block can be harvested (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowHarvestLevel());
        cfgshowCropPercentage = addModeConfig("showCropPercentage", "Show the growth level of crops (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowCropPercentage());
        cfgshowChestContents = addModeConfig("showChestContents", "Show chest contents (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowChestContents());
        cfgshowChestContentsDetailed = addModeConfig("showChestContentsDetailed", "Show chest contents in detail (0 = not, 1 = always, 2 = sneak), used only if number of items is below 'showItemDetailThresshold'", DEFAULT_CONFIG.getShowChestContentsDetailed());
        cfgshowRedstone = addModeConfig("showRedstone", "Show redstone (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowRedstone());
        cfgshowMobHealth = addModeConfig("showMobHealth", "Show mob health (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowMobHealth());
        cfgshowMobGrowth = addModeConfig("showMobGrowth", "Show time to adulthood for baby mobs (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowMobGrowth());
        cfgshowMobPotionEffects = addModeConfig("showMobPotionEffects", "Show mob potion effects (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowMobPotionEffects());
        cfgshowLeverSetting = addModeConfig("showLeverSetting", "Show lever/comparator/repeater settings (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowLeverSetting());
        cfgshowTankSetting = addModeConfig("showTankSetting", "Show tank setting (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowTankSetting());
        cfgshowBrewStandSetting = addModeConfig("showBrewStandSetting", "Show brewing stand setting (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowBrewStandSetting());
        cfgshowMobSpawnerSetting = addModeConfig("showMobSpawnerSetting", "Show mob spawner setting (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowMobSpawnerSetting());
        cfgshowAnimalOwnerSetting = addModeConfig("showAnimalOwnerSetting", "Show animal owner setting (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getAnimalOwnerSetting());
        cfgshowHorseStatSetting = addModeConfig("showHorseStatSetting", "Show horse stats setting (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getHorseStatSetting());
        cfgshowSilverfish = addModeConfig("showSilverfish", "Reveal monster eggs (0 = not, 1 = always, 2 = sneak)", DEFAULT_CONFIG.getShowSilverfish());
    }

    public static void setProbeNeeded(int probeNeeded) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
//        Config.needsProbe = probeNeeded;
//        cfg.get(CATEGORY_THEONEPROBE, "needsProbe", probeNeeded).set(probeNeeded);
//        cfg.save();
    }


    public static void setupStyleConfig() {
        leftX = CLIENT_BUILDER
                .comment("The distance to the left side of the screen. Use -1 if you don't want to set this")
                .defineInRange("boxLeftX", 5, -1, 10000);
        rightX = CLIENT_BUILDER
                .comment("The distance to the right side of the screen. Use -1 if you don't want to set this")
                .defineInRange("boxRightX", -1, -1, 10000);
        topY = CLIENT_BUILDER
                .comment("The distance to the top side of the screen. Use -1 if you don't want to set this")
                .defineInRange("boxTopY", 5, -1, 10000);
        bottomY = CLIENT_BUILDER
                .comment("The distance to the bottom side of the screen. Use -1 if you don't want to set this")
                .defineInRange("boxBottomY", -1, -1, 10000);

        cfgboxBorderColor = CLIENT_BUILDER
                .comment("Color of the border of the box (0 to disable)")
                .define("boxBorderColor", Integer.toHexString(boxBorderColor));
        cfgboxFillColor = CLIENT_BUILDER
                .comment("Color of the box (0 to disable)")
                .define("boxFillColor", Integer.toHexString(boxFillColor));

        boxThickness = CLIENT_BUILDER
                .comment("Thickness of the border of the box (0 to disable)")
                .defineInRange("boxThickness", 2, 0, 20);
        boxOffset = CLIENT_BUILDER
                .comment("How much the border should be offset (i.e. to create an 'outer' border)")
                .defineInRange("boxOffset", 0, 0, 20);

        showLiquids = CLIENT_BUILDER
                .comment("If true show liquid information when the probe hits liquid first")
                .define("showLiquids", false);
        isVisible = CLIENT_BUILDER
                .comment("Toggle default probe visibility (client can override)")
                .define("isVisible", true);
        holdKeyToMakeVisible = CLIENT_BUILDER
                .comment("If true then the probe hotkey must be held down to show the tooltip")
                .define("holdKeyToMakeVisible", false);

        tooltipScale = CLIENT_BUILDER
                .comment("The scale of the tooltips, 1 is default, 2 is smaller")
                .defineInRange("tooltipScale", 1.0, 0.4, 5.0);

        cfgchestContentsBorderColor = CLIENT_BUILDER
                .comment("Color of the border of the chest contents box (0 to disable)")
                .define("chestContentsBorderColor", Integer.toHexString(chestContentsBorderColor));

        showBreakProgress = CLIENT_BUILDER
                .comment("0 means don't show break progress, 1 is show as bar, 2 is show as text")
                .defineInRange("showBreakProgress", 1, 0, 2);
        harvestStyleVanilla = CLIENT_BUILDER
                .comment("true means shows harvestability with vanilla style icons")
                .define("harvestStyleVanilla", true);

        Map<TextStyleClass, ConfigValue<String>> newformat = new HashMap<>();
        for (TextStyleClass styleClass : textStyleClasses.keySet()) {
            ConfigValue<String> configValue = CLIENT_BUILDER
                    .comment("Text style. Use a comma delimited list with colors like: 'red', 'green', 'blue', ... or style codes like 'underline', 'bold', 'italic', 'strikethrough', ...\"")
                    .define("textStye" + styleClass.getReadableName(), textStyleClasses.get(styleClass));
            newformat.put(styleClass, configValue);
        }
        cfgtextStyleClasses = newformat;

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
//        Config.showLiquids = liquids;
//        cfg.get(CATEGORY_CLIENT, "showLiquids", showLiquids).set(liquids);
//        cfg.save();
    }

    public static void setVisible(boolean visible) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
//        Config.isVisible = visible;
//        cfg.get(CATEGORY_CLIENT, "isVisible", isVisible).set(visible);
//        cfg.save();
    }

    public static void setCompactEqualStacks(boolean compact) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
//        Config.compactEqualStacks = compact;
//        cfg.get(CATEGORY_CLIENT, "compactEqualStacks", compactEqualStacks).set(compact);
//        cfg.save();
    }

    public static void setPos(int leftx, int topy, int rightx, int bottomy) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
//        Config.leftX = leftx;
//        Config.topY = topy;
//        Config.rightX = rightx;
//        Config.bottomY = bottomy;
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
//        tooltipScale = scale;
//        cfg.get(CATEGORY_CLIENT, "tooltipScale", tooltipScale).set(tooltipScale);
//        cfg.save();
        updateDefaultOverlayStyle();
    }

    public static void setBoxStyle(int thickness, int borderColor, int fillcolor, int offset) {
        // @todo 1.13
//        Configuration cfg = TheOneProbe.config;
//        boxThickness = thickness;
//        boxBorderColor = borderColor;
//        boxFillColor = fillcolor;
//        boxOffset = offset;
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
                .borderThickness(boxThickness.get())
                .borderColor(boxBorderColor)
                .boxColor(boxFillColor)
                .borderOffset(boxOffset.get())
                .location(leftX.get(), rightX.get(), topY.get(), bottomY.get());
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
            for (String s : showContentsWithoutSneaking.get()) {
                inventoriesToShow.add(new ResourceLocation(s));
            }
        }
        return inventoriesToShow;
    }

    public static Set<ResourceLocation> getInventoriesToNotShow() {
        if (inventoriesToNotShow == null) {
            inventoriesToNotShow = new HashSet<>();
            for (String s : dontShowContentsUnlessSneaking.get()) {
                inventoriesToNotShow.add(new ResourceLocation(s));
            }
        }
        return inventoriesToNotShow;
    }

    public static Set<ResourceLocation> getDontSendNBTSet() {
        if (dontSendNBTSet == null) {
            dontSendNBTSet = new HashSet<>();
            for (String s : dontSendNBT.get()) {
                dontSendNBTSet.add(new ResourceLocation(s));
            }
        }
        return dontSendNBTSet;
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        TheOneProbe.logger.debug("Loaded {} config file {}", TheOneProbe.MODID, configEvent.getConfig().getFileName());
        resolveConfigs();

    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
        TheOneProbe.logger.fatal(CORE, "{} config just got changed on the file system!", TheOneProbe.MODID);
        resolveConfigs();
    }

    private static void resolveConfigs() {
        DEFAULT_CONFIG.setRFMode(defaultRFMode.get());
        DEFAULT_CONFIG.setTankMode(defaultTankMode.get());
        rfbarFilledColor = parseColor(cfgRfbarFilledColor.get());
        rfbarAlternateFilledColor = parseColor(cfgRfbarAlternateFilledColor.get());
        rfbarBorderColor = parseColor(cfgRfbarBorderColor.get());
        tankbarFilledColor = parseColor(cfgTankbarFilledColor.get());
        tankbarAlternateFilledColor = parseColor(cfgTankbarAlternateFilledColor.get());
        tankbarBorderColor = parseColor(cfgTankbarBorderColor.get());

        boxBorderColor = parseColor(cfgboxBorderColor.get());
        boxFillColor = parseColor(cfgboxFillColor.get());
        chestContentsBorderColor = parseColor(cfgchestContentsBorderColor.get());

        DEFAULT_CONFIG.showModName(cfgshowModName.get());
        DEFAULT_CONFIG.showHarvestLevel(cfgshowHarvestLevel.get());
        DEFAULT_CONFIG.showCanBeHarvested(cfgshowCanBeHarvested.get());
        DEFAULT_CONFIG.showCropPercentage(cfgshowCropPercentage.get());
        DEFAULT_CONFIG.showChestContents(cfgshowChestContents.get());
        DEFAULT_CONFIG.showChestContentsDetailed(cfgshowChestContentsDetailed.get());
        DEFAULT_CONFIG.showRedstone(cfgshowRedstone.get());
        DEFAULT_CONFIG.showMobHealth(cfgshowMobHealth.get());
        DEFAULT_CONFIG.showMobGrowth(cfgshowMobGrowth.get());
        DEFAULT_CONFIG.showMobPotionEffects(cfgshowMobPotionEffects.get());
        DEFAULT_CONFIG.showLeverSetting(cfgshowLeverSetting.get());
        DEFAULT_CONFIG.showTankSetting(cfgshowTankSetting.get());
        DEFAULT_CONFIG.showBrewStandSetting(cfgshowBrewStandSetting.get());
        DEFAULT_CONFIG.showMobSpawnerSetting(cfgshowMobSpawnerSetting.get());
        DEFAULT_CONFIG.showAnimalOwnerSetting(cfgshowAnimalOwnerSetting.get());
        DEFAULT_CONFIG.showHorseStatSetting(cfgshowHorseStatSetting.get());
        DEFAULT_CONFIG.showSilverfish(cfgshowSilverfish.get());

        inventoriesToShow = null;
        inventoriesToNotShow = null;
        dontSendNBT = null;

        for (Map.Entry<TextStyleClass, ConfigValue<String>> entry : cfgtextStyleClasses.entrySet()) {
            textStyleClasses.put(entry.getKey(), entry.getValue().get());
        }

    }
}
