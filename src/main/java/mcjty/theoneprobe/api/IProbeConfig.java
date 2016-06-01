package mcjty.theoneprobe.api;

/**
 * This inerface represents the default config for The One Probe.
 */
public interface IProbeConfig {

    /**
     * Control how RF should be shown
     * @param showRF 0 = not, 1 = show as bar, 2 = show as text
     */
    IProbeConfig setRFMode(int showRF);

    int getRFMode();
}
