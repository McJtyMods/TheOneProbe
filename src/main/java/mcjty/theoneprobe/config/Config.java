package mcjty.theoneprobe.config;


import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.styles.DefaultOverlayStyle;
import mcjty.theoneprobe.items.IEnumConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Predicate;

import static mcjty.theoneprobe.api.TextStyleClass.*;

public class Config {

    private static final Builder COMMON_BUILDER = new Builder();
    private static final Builder CLIENT_BUILDER = new Builder();

    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec CLIENT_CONFIG;


    public static final int PROBE_NOTNEEDED = 0;
    public static final int PROBE_NEEDED = 1;
    public static final int PROBE_NEEDEDHARD = 2;
    public static final int PROBE_NEEDEDFOREXTENDED = 3;
    public static IntValue needsProbe;

    public static BooleanValue extendedInMain;
    public static IEnumConfig<NumberFormat> rfFormat;
    public static IEnumConfig<NumberFormat> tankFormat;
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
    private static ConfigValue<List<? extends String>> showContentsWithoutSneaking;
    private static ConfigValue<List<? extends String>> dontShowContentsUnlessSneaking;
    private static ConfigValue<List<? extends String>> dontSendNBT;
    private static ConfigValue<List<? extends String>> tooltypeTags;
    private static ConfigValue<List<? extends String>> harvestabilityTags;
    private static ConfigValue<List<? extends String>> blacklistEntities;
    private static Set<ResourceLocation> inventoriesToShow = null;
    private static Set<ResourceLocation> inventoriesToNotShow = null;
    private static Set<ResourceLocation> dontSendNBTSet = null;
    private static Set<Predicate<ResourceLocation>> blacklistEntitiesSet = null;
    private static Map<ResourceLocation, String> tooltypeTagsSet = null;
    private static Map<ResourceLocation, String> harvestabilityTagsSet = null;

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


    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowModName;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowHarvestLevel;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowCanBeHarvested;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowCropPercentage;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowChestContents;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowChestContentsDetailed;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowRedstone;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowMobHealth;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowMobGrowth;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowMobPotionEffects;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowLeverSetting;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowTankSetting;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowBrewStandSetting;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowMobSpawnerSetting;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowAnimalOwnerSetting;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowHorseStatSetting;
    private static IEnumConfig<IProbeConfig.ConfigMode> cfgshowSilverfish;

    private static ConfigValue<List<? extends String>> renderBlacklist;
    private static Set<ResourceLocation> renderBlacklistSet = null;


    public static final Map<TextStyleClass, String> defaultTextStyleClasses = new HashMap<>();
    public static Map<TextStyleClass, ConfigValue<String>> cfgtextStyleClasses = new HashMap<>();
    public static Map<TextStyleClass, String> textStyleClasses;

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
        defaultTextStyleClasses.put(HIGHLIGHTED, "gold");
        textStyleClasses = new HashMap<>(defaultTextStyleClasses);

        DEFAULT_CONFIG = new ProbeConfig();

        COMMON_BUILDER.comment("General configuration");
        CLIENT_BUILDER.comment("General configuration");
        loggingThrowableTimeout = COMMON_BUILDER
                .comment("How much time (ms) to wait before reporting an exception again")
                .defineInRange("loggingThrowableTimeout", 20000, 1, 10000000);

        renderBlacklist = CLIENT_BUILDER
                .comment("This is a list of entities that will not be rendered by TOP. This option also works if it is set client-side alone")
                .defineList("renderBlacklist", Collections.emptyList(), s -> s instanceof String);

        needsProbe = COMMON_BUILDER
                .comment("Is the probe needed to show the tooltip? 0 = no, 1 = yes, 2 = yes and clients cannot override, 3 = probe needed for extended info only")
                .defineInRange("needsProbe", PROBE_NEEDEDFOREXTENDED, 0, 3);

        extendedInMain = COMMON_BUILDER
                .comment("If true the probe will automatically show extended information if it is in your main hand (so not required to sneak)")
                .define("extendedInMain", false);
        supportBaubles = COMMON_BUILDER
                .comment("If true there will be a bauble version of the probe if baubles is present")
                .define("supportBaubles", true);
        spawnNote = COMMON_BUILDER
                .comment("If true there will be a readme note for first-time players")
                .define("spawnNote", true);
        showCollarColor = COMMON_BUILDER
                .comment("If true show the color of the collar of a wolf")
                .define("showCollarColor", true);

        defaultRFMode = COMMON_BUILDER
                .comment("How to display RF: 0 = do not show, 1 = show in a bar, 2 = show as text")
                .defineInRange("showRF", DEFAULT_CONFIG.getRFMode(), 0, 2);
        defaultTankMode = COMMON_BUILDER
                .comment("How to display tank contents: 0 = do not show, 1 = show in fluid bar, 2 = show in a bar, 3 = show as text")
                .defineInRange("showTank", DEFAULT_CONFIG.getRFMode(), 0, 3);
        rfFormat = addEnumConfig(COMMON_BUILDER, "rfFormat", "Format for displaying RF",
                NumberFormat.COMPACT, NumberFormat.COMMAS, NumberFormat.COMPACT, NumberFormat.FULL, NumberFormat.NONE);
        tankFormat = addEnumConfig(COMMON_BUILDER, "tankFormat", "Format for displaying tank contents",
                NumberFormat.COMPACT, NumberFormat.COMMAS, NumberFormat.COMPACT, NumberFormat.FULL, NumberFormat.NONE);

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

        showDebugInfo = COMMON_BUILDER
                .comment("If true show debug info with creative probe")
                .define("showDebugInfo", true);
        compactEqualStacks = COMMON_BUILDER
                .comment("If true equal stacks will be compacted in the chest contents overlay")
                .define("compactEqualStacks", true);

        cfgRfbarFilledColor = COMMON_BUILDER
                .comment("Color for the RF bar")
                .define("rfbarFilledColor", Integer.toHexString(rfbarFilledColor));
        cfgRfbarAlternateFilledColor = COMMON_BUILDER
                .comment("Alternate color for the RF bar")
                .define("rfbarAlternateFilledColor", Integer.toHexString(rfbarAlternateFilledColor));
        cfgRfbarBorderColor = COMMON_BUILDER
                .comment("Color for the RF bar border")
                .define("rfbarBorderColor", Integer.toHexString(rfbarBorderColor));
        cfgTankbarFilledColor = COMMON_BUILDER
                .comment("Color for the tank bar")
                .define("tankbarFilledColor", Integer.toHexString(tankbarFilledColor));
        cfgTankbarAlternateFilledColor = COMMON_BUILDER
                .comment("Alternate color for the tank bar")
                .define("tankbarAlternateFilledColor", Integer.toHexString(tankbarAlternateFilledColor));
        cfgTankbarBorderColor = COMMON_BUILDER
                .comment("Color for the tank bar border")
                .define("tankbarBorderColor", Integer.toHexString(tankbarBorderColor));

        showItemDetailThresshold = COMMON_BUILDER
            .comment("If the number of items in an inventory is lower or equal then this number then more info is shown")
            .defineInRange("showItemDetailThresshold", 4, 0, 20);
        showSmallChestContentsWithoutSneaking = COMMON_BUILDER
                .comment("The maximum amount of slots (empty or not) to show without sneaking")
                .defineInRange("showSmallChestContentsWithoutSneaking", 0, 0, 1000);
        showContentsWithoutSneaking = COMMON_BUILDER
                .comment("A list of blocks for which we automatically show chest contents even if not sneaking")
                .defineList("showContentsWithoutSneaking", new ArrayList<>(Arrays.asList("storagedrawers:basicdrawers", "storagedrawersextra:extra_drawers")),
                        s -> s instanceof String);
        dontShowContentsUnlessSneaking = COMMON_BUILDER
                .comment("A list of blocks for which we don't show chest contents automatically except if sneaking")
                .defineList("dontShowContentsUnlessSneaking", new ArrayList<>(),
                        s -> s instanceof String);

        dontSendNBT = COMMON_BUILDER
                .comment("A list of blocks for which we don't send NBT over the network. This is mostly useful for blocks that have HUGE NBT in their pickblock (itemstack)")
                .defineList("dontSendNBT", new ArrayList<>(),
                        s -> s instanceof String);
        blacklistEntities = COMMON_BUILDER
                .comment("A list of either <modid>:<entityid> to disable the tooltip for specific entities. Can also be a single <modid> to disable an entire mod. Or it can also be '*' to disable everything")
                .defineList("blacklistEntities", new ArrayList<>(),
                        s -> s instanceof String);
        tooltypeTags = COMMON_BUILDER
                .comment("A list of <tag>=<name> containing all tooltype tags with their associated name to display")
                .defineList("tooltypeTags", List.of("minecraft:mineable/axe=Axe", "minecraft:mineable/pickaxe=Pickaxe", "minecraft:mineable/shovel=Shovel", "minecraft:mineable/hoe=Hoe"),
                        s -> s instanceof String);
        harvestabilityTags = COMMON_BUILDER
                .comment("A list of <tag>=<name> containing all harvestability tags with their associated name to display")
                .defineList("harvestabilityTags", List.of("forge:needs_wood_tool=Wood", "forge:needs_gold_tool=Gold",
                        "minecraft:needs_stone_tool=Stone", "minecraft:needs_iron_tool=Iron", "minecraft:needs_diamond_tool=Diamond",
                        "forge:needs_netherite_tool=Netherite"),
                        s -> s instanceof String);

        setupStyleConfig();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    // @todo 1.13: this version doesn't work yet due to forge/toml error
//    private static <T extends Enum<T>> IEnumConfig<T> addEnumConfig(Builder builder, String path, String comment,
//                                                                    T def, T... values) {
//        ConfigValue<T> configValue = builder.comment(comment).defineEnum(path, def, values);
//        return () -> configValue.get();
//    }
    // @todo 1.13: temporary workaround
    private static <T extends Enum<T>> IEnumConfig<T> addEnumConfig(Builder builder, String path, String comment,
                                                                    T def, T... values) {
        ConfigValue<String> configValue = builder.comment(comment).define(path, def.name());
        return new IEnumConfig<T>() {
            @Override
            public T get() {
                String s = configValue.get();
                for (T value : values) {
                    if (value.name().equals(s)) {
                        return value;
                    }
                }
                return null;
            }

            @Override
            public T getDefault() {
                String s = configValue.getDefault();
                for (T value : values) {
                    if (value.name().equals(s)) {
                        return value;
                    }
                }
                return null;
            }
        };
    }

    private static IEnumConfig<IProbeConfig.ConfigMode> addModeConfig(String path, String comment, IProbeConfig.ConfigMode def) {
        return addEnumConfig(CLIENT_BUILDER, path, comment, def, IProbeConfig.ConfigMode.NORMAL, IProbeConfig.ConfigMode.EXTENDED, IProbeConfig.ConfigMode.NOT);
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
        Config.needsProbe.set(probeNeeded);
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

        CLIENT_BUILDER.push("style");

        Map<TextStyleClass, ConfigValue<String>> newformat = new HashMap<>();
        for (Map.Entry<TextStyleClass, String> entry : textStyleClasses.entrySet()) {
            TextStyleClass styleClass = entry.getKey();
            ConfigValue<String> configValue = CLIENT_BUILDER
                    .comment("Text style. Use a comma delimited list with colors like: 'red', 'green', 'blue', ... or style codes like 'underline', 'bold', 'italic', 'strikethrough', ...\"")
                    .define(styleClass.getReadableName(), entry.getValue());
            newformat.put(styleClass, configValue);
        }
        cfgtextStyleClasses = newformat;

        CLIENT_BUILDER.pop();

        // @todo 1.13
//        extendedInMain = cfg.getBoolean("extendedInMain", CATEGORY_CLIENT, extendedInMain, "If true the probe will automatically show extended information if it is in your main hand (so not required to sneak)");
    }

    public static void setTextStyle(TextStyleClass styleClass, String style) {
        Config.textStyleClasses.put(styleClass, style);
        cfgtextStyleClasses.get(styleClass).set(style);
    }

    public static void setExtendedInMain(boolean extendedInMain) {
        Config.extendedInMain.set(extendedInMain);
    }

    public static void setLiquids(boolean liquids) {
        Config.showLiquids.set(liquids);
    }

    public static void setVisible(boolean visible) {
        Config.isVisible.set(visible);
    }

    public static void setCompactEqualStacks(boolean compact) {
        Config.compactEqualStacks.set(compact);
    }

    public static void setPos(int leftx, int topy, int rightx, int bottomy) {
        Config.leftX.set(leftx);
        Config.topY.set(topy);
        Config.rightX.set(rightx);
        Config.bottomY.set(bottomy);
        updateDefaultOverlayStyle();
    }

    public static void setScale(float scale) {
        tooltipScale.set((double) scale);
        updateDefaultOverlayStyle();
    }

    public static void setBoxStyle(int thickness, int borderColor, int fillcolor, int offset) {
        boxThickness.set(thickness);
        boxBorderColor = borderColor;
        boxFillColor = fillcolor;
        cfgboxBorderColor.set(Integer.toHexString(boxBorderColor));
        cfgboxFillColor.set(Integer.toHexString(boxFillColor));
        boxOffset.set(offset);
        updateDefaultOverlayStyle();
    }

    private static String configToTextFormat(String input) {
        if ("context".equals(input)) {
            return "context";
        }
        StringBuilder builder = new StringBuilder();
        String[] splitted = StringUtils.split(input, ',');
        for (String s : splitted) {
            ChatFormatting format = ChatFormatting.getByName(s);
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

    public static Map<ResourceLocation, String> getTooltypeTags() {
        if (tooltypeTagsSet == null) {
            tooltypeTagsSet = new HashMap<>();
            for (String s : tooltypeTags.get()) {
                String[] splitted = StringUtils.split(s, '=');
                tooltypeTagsSet.put(new ResourceLocation(splitted[0]), splitted[1]);
            }
        }
        return tooltypeTagsSet;
    }

    public static Map<ResourceLocation, String> getHarvestabilityTags() {
        if (harvestabilityTagsSet == null) {
            harvestabilityTagsSet = new HashMap<>();
            for (String s : harvestabilityTags.get()) {
                String[] splitted = StringUtils.split(s, '=');
                harvestabilityTagsSet.put(new ResourceLocation(splitted[0]), splitted[1]);
            }
        }
        return harvestabilityTagsSet;
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

    public static Set<Predicate<ResourceLocation>> getEntityBlacklist() {
        if (blacklistEntitiesSet == null) {
            blacklistEntitiesSet = new HashSet<>();
            for (String s : blacklistEntities.get()) {
                if ("*".equals(s)) {
                    blacklistEntitiesSet.add(rl -> true);
                } else if (s.contains(":")) {
                    ResourceLocation wanted = new ResourceLocation(s);
                    blacklistEntitiesSet.add(rl -> rl.equals(wanted));
                } else {
                    blacklistEntitiesSet.add(rl -> rl.getNamespace().equals(s));
                }
            }
        }
        return blacklistEntitiesSet;
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

    private static <T> T getOrDefault(ModConfigSpec spec, ConfigValue<T> config) {
        if (spec.isLoaded()) {
            return config.get();
        } else {
            return config.getDefault();
        }
    }

    private static IProbeConfig.ConfigMode getOrDefault(ModConfigSpec spec, IEnumConfig<IProbeConfig.ConfigMode> config) {
        if (spec.isLoaded()) {
            return config.get();
        } else {
            return config.getDefault();
        }
    }

    public static void resolveConfigs() {
        DEFAULT_CONFIG.setRFMode(getOrDefault(COMMON_CONFIG, defaultRFMode));
        DEFAULT_CONFIG.setTankMode(getOrDefault(COMMON_CONFIG, defaultTankMode));
        rfbarFilledColor = parseColor(getOrDefault(COMMON_CONFIG, cfgRfbarFilledColor));
        rfbarAlternateFilledColor = parseColor(getOrDefault(COMMON_CONFIG, cfgRfbarAlternateFilledColor));
        rfbarBorderColor = parseColor(getOrDefault(COMMON_CONFIG, cfgRfbarBorderColor));
        tankbarFilledColor = parseColor(getOrDefault(COMMON_CONFIG, cfgTankbarFilledColor));
        tankbarAlternateFilledColor = parseColor(getOrDefault(COMMON_CONFIG, cfgTankbarAlternateFilledColor));
        tankbarBorderColor = parseColor(getOrDefault(COMMON_CONFIG, cfgTankbarBorderColor));

        boxBorderColor = parseColor(getOrDefault(CLIENT_CONFIG, cfgboxBorderColor));
        boxFillColor = parseColor(getOrDefault(CLIENT_CONFIG, cfgboxFillColor));
        chestContentsBorderColor = parseColor(getOrDefault(CLIENT_CONFIG, cfgchestContentsBorderColor));

        DEFAULT_CONFIG.showModName(getOrDefault(CLIENT_CONFIG, cfgshowModName));
        DEFAULT_CONFIG.showHarvestLevel(getOrDefault(CLIENT_CONFIG, cfgshowHarvestLevel));
        DEFAULT_CONFIG.showCanBeHarvested(getOrDefault(CLIENT_CONFIG, cfgshowCanBeHarvested));
        DEFAULT_CONFIG.showCropPercentage(getOrDefault(CLIENT_CONFIG, cfgshowCropPercentage));
        DEFAULT_CONFIG.showChestContents(getOrDefault(CLIENT_CONFIG, cfgshowChestContents));
        DEFAULT_CONFIG.showChestContentsDetailed(getOrDefault(CLIENT_CONFIG, cfgshowChestContentsDetailed));
        DEFAULT_CONFIG.showRedstone(getOrDefault(CLIENT_CONFIG, cfgshowRedstone));
        DEFAULT_CONFIG.showMobHealth(getOrDefault(CLIENT_CONFIG, cfgshowMobHealth));
        DEFAULT_CONFIG.showMobGrowth(getOrDefault(CLIENT_CONFIG, cfgshowMobGrowth));
        DEFAULT_CONFIG.showMobPotionEffects(getOrDefault(CLIENT_CONFIG, cfgshowMobPotionEffects));
        DEFAULT_CONFIG.showLeverSetting(getOrDefault(CLIENT_CONFIG, cfgshowLeverSetting));
        DEFAULT_CONFIG.showTankSetting(getOrDefault(CLIENT_CONFIG, cfgshowTankSetting));
        DEFAULT_CONFIG.showBrewStandSetting(getOrDefault(CLIENT_CONFIG, cfgshowBrewStandSetting));
        DEFAULT_CONFIG.showMobSpawnerSetting(getOrDefault(CLIENT_CONFIG, cfgshowMobSpawnerSetting));
        DEFAULT_CONFIG.showAnimalOwnerSetting(getOrDefault(CLIENT_CONFIG, cfgshowAnimalOwnerSetting));
        DEFAULT_CONFIG.showHorseStatSetting(getOrDefault(CLIENT_CONFIG, cfgshowHorseStatSetting));
        DEFAULT_CONFIG.showSilverfish(getOrDefault(CLIENT_CONFIG, cfgshowSilverfish));

        inventoriesToShow = null;
        inventoriesToNotShow = null;
        dontSendNBTSet = null;

        for (Map.Entry<TextStyleClass, ConfigValue<String>> entry : cfgtextStyleClasses.entrySet()) {
            if (CLIENT_CONFIG.isLoaded()) {
                textStyleClasses.put(entry.getKey(), entry.getValue().get());
            } else {
                textStyleClasses.put(entry.getKey(), entry.getValue().getDefault());
            }
        }

    }

    public static boolean isBlacklistForRendering(ResourceLocation id) {
        if (renderBlacklistSet == null) {
            renderBlacklistSet = new HashSet<>();
            for (String s : renderBlacklist.get()) {
                renderBlacklistSet.add(new ResourceLocation(s));
            }
        }
        return renderBlacklistSet.contains(id);
    }

    public static void onLoad(ModConfigEvent.Loading event) {
        renderBlacklistSet = null;
    }

    public static void onReload(ModConfigEvent.Reloading event) {
        renderBlacklistSet = null;
    }
}
