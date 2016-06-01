package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeConfig;

public class ProbeConfig implements IProbeConfig {

    private int showRF = 1;

    /// Make a lazy copy of this probe config.
    public IProbeConfig lazyCopy() {
        return new LazyProbeConfig(this);
    }

    @Override
    public IProbeConfig setRFMode(int showRF) {
        this.showRF = showRF;
        return this;
    }

    @Override
    public int getRFMode() {
        return showRF;
    }
}
