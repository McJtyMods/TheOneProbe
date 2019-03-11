package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.*;

import java.util.*;

public class TheOneProbeImp implements ITheOneProbe {

    public static int ELEMENT_TEXT;
    public static int ELEMENT_ITEM;
    public static int ELEMENT_PROGRESS;
    public static int ELEMENT_HORIZONTAL;
    public static int ELEMENT_VERTICAL;
    public static int ELEMENT_ENTITY;
    public static int ELEMENT_ICON;
    public static int ELEMENT_ITEMLABEL;

    private List<IProbeConfigProvider> configProviders = new ArrayList<>();

    private List<IProbeInfoProvider> providers = new ArrayList<>();
    private List<IProbeInfoEntityProvider> entityProviders = new ArrayList<>();
    private List<IBlockDisplayOverride> blockOverrides = new ArrayList<>();
    private List<IEntityDisplayOverride> entityOverrides = new ArrayList<>();
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
        ELEMENT_ENTITY = TheOneProbe.theOneProbeImp.registerElementFactory(ElementEntity::new);
        ELEMENT_ICON = TheOneProbe.theOneProbeImp.registerElementFactory(ElementIcon::new);
        ELEMENT_ITEMLABEL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementItemLabel::new);
    }

    private int findProvider(String id) {
        for (int i = 0 ; i < providers.size() ; i++) {
            if (id.equals(providers.get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void registerProvider(IProbeInfoProvider provider) {
        int idx = findProvider(provider.getID());
        if (idx != -1) {
            providers.set(idx, provider);
        } else {
            providers.add(provider);
        }
    }

    private int findEntityProvider(String id) {
        for (int i = 0 ; i < entityProviders.size() ; i++) {
            if (id.equals(entityProviders.get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void registerEntityProvider(IProbeInfoEntityProvider provider) {
        int idx = findEntityProvider(provider.getID());
        if (idx != -1) {
            entityProviders.set(idx, provider);
        } else {
            entityProviders.add(provider);
        }
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

    public List<IProbeInfoEntityProvider> getEntityProviders() {
        return entityProviders;
    }

    private IProbeInfoProvider getProviderByID(String id) {
        for (IProbeInfoProvider provider : providers) {
            if (provider.getID().equals(id)) {
                return provider;
            }
        }
        return null;
    }

    private IProbeInfoEntityProvider getEntityProviderByID(String id) {
        for (IProbeInfoEntityProvider provider : entityProviders) {
            if (provider.getID().equals(id)) {
                return provider;
            }
        }
        return null;
    }

    public void configureProviders(String[] sortedProviders, Set<String> excludedProviders) {
        List<IProbeInfoProvider> newProviders = new ArrayList<>();
        for (String id : sortedProviders) {
            if (!excludedProviders.contains(id)) {
                IProbeInfoProvider provider = getProviderByID(id);
                if (provider != null) {
                    newProviders.add(provider);
                }
            }
        }

        // Add all providers that are not in the list of sortedProviders and are also not
        // excluded.
        for (IProbeInfoProvider provider : providers) {
            if ((!newProviders.contains(provider)) && !excludedProviders.contains(provider.getID())) {
                newProviders.add(provider);
            }
        }

        providers = newProviders;
    }

    public void configureEntityProviders(String[] sortedProviders, Set<String> excludedProviders) {
        List<IProbeInfoEntityProvider> newProviders = new ArrayList<>();
        for (String id : sortedProviders) {
            if (!excludedProviders.contains(id)) {
                IProbeInfoEntityProvider provider = getEntityProviderByID(id);
                if (provider != null) {
                    newProviders.add(provider);
                }
            }
        }

        // Add all providers that are not in the list of sortedProviders and are also not
        // excluded.
        for (IProbeInfoEntityProvider provider : entityProviders) {
            if ((!newProviders.contains(provider)) && !excludedProviders.contains(provider.getID())) {
                newProviders.add(provider);
            }
        }

        entityProviders = newProviders;
    }

    @Override
    public int registerElementFactory(IElementFactory factory) {
        factories.put(lastId, factory);
        int id = lastId;
        lastId++;
        return id;
    }

    @Override
    public IOverlayRenderer getOverlayRenderer() {
        return new DefaultOverlayRenderer();
    }

    @Override
    public IProbeConfig createProbeConfig() {
        return ConfigSetup.getDefaultConfig().lazyCopy();
    }

    @Override
    public void registerProbeConfigProvider(IProbeConfigProvider provider) {
        configProviders.add(provider);
    }

    public List<IProbeConfigProvider> getConfigProviders() {
        return configProviders;
    }

    @Override
    public void registerBlockDisplayOverride(IBlockDisplayOverride override) {
        blockOverrides.add(override);
    }

    public List<IBlockDisplayOverride> getBlockOverrides() {
        return blockOverrides;
    }

    @Override
    public void registerEntityDisplayOverride(IEntityDisplayOverride override) {
        entityOverrides.add(override);
    }

    public List<IEntityDisplayOverride> getEntityOverrides() {
        return entityOverrides;
    }
}
