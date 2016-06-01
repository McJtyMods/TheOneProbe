package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeConfig;

public class LazyProbeConfig implements IProbeConfig {

    private IProbeConfig original;
    private boolean dirty = false;

    public LazyProbeConfig(IProbeConfig original) {
        this.original = original;
    }

    private IProbeConfig realCopy() {
        if (!dirty) {
            dirty = true;
            original = new ProbeConfig()
                    .setRFMode(original.getRFMode());
        }
        return original;
    }

    @Override
    public IProbeConfig setRFMode(int showRF) {
        realCopy().setRFMode(showRF);
        return this;
    }

    @Override
    public int getRFMode() {
        return original.getRFMode();
    }
}
