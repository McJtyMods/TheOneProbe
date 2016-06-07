package mcjty.theoneprobe.api;

/**
 * This inerface represents the default config for The One Probe.
 */
public interface IProbeConfig {

    public static enum ConfigMode {
        NOT,            // Don't show
        NORMAL,         // Show
        EXTENDED        // Shgw only when sneaking
    }

    /**
     * Control how RF should be shown
     * @param mode 0 = not, 1 = show as bar, 2 = show as text
     */
    IProbeConfig setRFMode(int mode);
    int getRFMode();

    IProbeConfig showModName(ConfigMode mode);
    ConfigMode getShowModName();

    IProbeConfig showHarvestLevel(ConfigMode mode);
    ConfigMode getShowHarvestLevel();

    IProbeConfig showCropPercentage(ConfigMode mode);
    ConfigMode getShowCropPercentage();

    IProbeConfig showChestContents(ConfigMode mode);
    ConfigMode getShowChestContents();

    IProbeConfig showRedstone(ConfigMode mode);
    ConfigMode getShowRedstone();

    IProbeConfig showMobHealth(ConfigMode mode);
    ConfigMode getShowMobHealth();

    IProbeConfig showMobPotionEffects(ConfigMode mode);
    ConfigMode getShowMobPotionEffects();
}
