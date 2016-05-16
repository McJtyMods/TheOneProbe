package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.ArrayList;
import java.util.List;

public class TheOneProbeImp implements ITheOneProbe {

    private List<IProbeInfoProvider> providers = new ArrayList<>();

    public TheOneProbeImp() {
        providers.add(new DefaultProbeInfoProvider());
    }

    @Override
    public void registerProvider(IProbeInfoProvider provider) {
        providers.add(provider);
    }

    public ProbeInfo create() {
        return new ProbeInfo();
    }

    public List<IProbeInfoProvider> getProviders() {
        return providers;
    }
}
