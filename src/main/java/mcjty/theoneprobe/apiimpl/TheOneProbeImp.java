package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheOneProbeImp implements ITheOneProbe {

    private List<IProbeInfoProvider> providers = new ArrayList<>();
    private Map<Integer,IElementFactory> factories = new HashMap<>();
    private int lastId = 0;

    public TheOneProbeImp() {
    }

    @Override
    public void registerProvider(IProbeInfoProvider provider) {
        providers.add(provider);
    }

    @Override
    public IElementFactory getElementFactory(int id) {
        return factories.get(id);
    }

    public ProbeInfo create() {
        return new ProbeInfo();
    }

    public List<IProbeInfoProvider> getProviders() {
        return providers;
    }

    @Override
    public int registerElementFactory(IElementFactory factory) {
        factories.put(lastId, factory);
        int id = lastId;
        lastId++;
        return id;
    }
}
