package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheOneProbeImp implements ITheOneProbe {

    public static int ELEMENT_TEXT;
    public static int ELEMENT_ITEM;
    public static int ELEMENT_PROGRESS;
    public static int ELEMENT_HORIZONTAL;
    public static int ELEMENT_VERTICAL;


    private List<IProbeInfoProvider> providers = new ArrayList<>();
    private Map<Integer,IElementFactory> factories = new HashMap<>();
    private int lastId = 0;

    public TheOneProbeImp() {
    }

    public static void registerElements() {
        ELEMENT_TEXT = TheOneProbe.theOneProbeImp.registerElementFactory(ElementText::new);
        ELEMENT_ITEM = TheOneProbe.theOneProbeImp.registerElementFactory(ElementItemStack::new);
        ELEMENT_PROGRESS = TheOneProbe.theOneProbeImp.registerElementFactory(ElementProgress::new);
        ELEMENT_HORIZONTAL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementHorizontal::new);
        ELEMENT_VERTICAL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementVertical::new);
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
