package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.ArrayList;
import java.util.List;

public class TheOneProbe implements ITheOneProbe {

    private List<IProbeInfoProvider> providers = new ArrayList<>();

    @Override
    public void registerProvider(IProbeInfoProvider provider) {
        providers.add(provider);
    }

    @Override
    public IProbeInfo create() {
        return new ProbeInfo();
    }
}
