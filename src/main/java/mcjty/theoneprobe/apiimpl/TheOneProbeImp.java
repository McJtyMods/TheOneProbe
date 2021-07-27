package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.*;
import mcjty.theoneprobe.apiimpl.styles.StyleManager;
import mcjty.theoneprobe.config.Config;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Function;

public class TheOneProbeImp implements ITheOneProbe {

    public static final ResourceLocation ELEMENT_TEXT = new ResourceLocation(TheOneProbe.MODID, "text");
    public static final ResourceLocation ELEMENT_ITEM = new ResourceLocation(TheOneProbe.MODID, "item");
    public static final ResourceLocation ELEMENT_PROGRESS = new ResourceLocation(TheOneProbe.MODID, "progress");
    public static final ResourceLocation ELEMENT_HORIZONTAL = new ResourceLocation(TheOneProbe.MODID, "hor");
    public static final ResourceLocation ELEMENT_VERTICAL = new ResourceLocation(TheOneProbe.MODID, "ver");
    public static final ResourceLocation ELEMENT_ENTITY = new ResourceLocation(TheOneProbe.MODID, "ent");
    public static final ResourceLocation ELEMENT_ICON = new ResourceLocation(TheOneProbe.MODID, "icon");
    public static final ResourceLocation ELEMENT_ITEMLABEL = new ResourceLocation(TheOneProbe.MODID, "itemlabel");
    public static final ResourceLocation ELEMENT_TANK = new ResourceLocation(TheOneProbe.MODID, "tank");
    public static final ResourceLocation ELEMENT_PADDING = new ResourceLocation(TheOneProbe.MODID, "pad");

    private final StyleManager styleManager = new StyleManager();
    private List<IProbeConfigProvider> configProviders = new ArrayList<>();

    private List<IProbeInfoProvider> providers = new ArrayList<>();
    private List<IProbeInfoEntityProvider> entityProviders = new ArrayList<>();
    private List<IBlockDisplayOverride> blockOverrides = new ArrayList<>();
    private List<IEntityDisplayOverride> entityOverrides = new ArrayList<>();
    private Map<ResourceLocation, IElementFactory> factories = new HashMap<>();

    public TheOneProbeImp() {
    }

    private static IElementFactory create(ResourceLocation id, Function<FriendlyByteBuf, IElement> factory) {
        return new IElementFactory() {
            @Override
            public IElement createElement(FriendlyByteBuf buf) {
                return factory.apply(buf);
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }
        };
    }

    public static void registerElements() {
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_TEXT, ElementText::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_ITEM, ElementItemStack::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_PROGRESS, ElementProgress::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_HORIZONTAL, ElementHorizontal::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_VERTICAL, ElementVertical::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_ENTITY, ElementEntity::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_ICON, ElementIcon::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_ITEMLABEL, ElementItemLabel::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_TANK, ElementTank::new));
        TheOneProbe.theOneProbeImp.registerElementFactory(create(ELEMENT_PADDING, ElementPadding::new));
    }

    private int findProvider(ResourceLocation id) {
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
    public IElementFactory getElementFactory(ResourceLocation id) {
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

    private IProbeInfoProvider getProviderByID(ResourceLocation id) {
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

    public void configureProviders(ResourceLocation[] sortedProviders, Set<String> excludedProviders) {
        List<IProbeInfoProvider> newProviders = new ArrayList<>();
        for (ResourceLocation id : sortedProviders) {
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
    public void registerElementFactory(IElementFactory factory) {
        factories.put(factory.getId(), factory);
    }

    @Override
    public IOverlayRenderer getOverlayRenderer() {
        return new DefaultOverlayRenderer();
    }

    @Override
    public IProbeConfig createProbeConfig() {
        return Config.getDefaultConfig().lazyCopy();
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

    @Override
    public IStyleManager getStyleManager() {
        return styleManager;
    }
}
