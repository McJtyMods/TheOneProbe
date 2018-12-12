package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.providers.*;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.network.PacketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonProxy {

    public void preInit() {
        registerCapabilities();
        TheOneProbeImp.registerElements();
        TheOneProbe.theOneProbeImp.registerProvider(new DefaultProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new DebugProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new BlockProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DefaultProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DebugProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new EntityProbeInfoEntityProvider());

        readMainConfig();
        PacketHandler.registerMessages("theoneprobe");
    }

    private static void registerCapabilities(){
        // @todo fabric
//        CapabilityManager.INSTANCE.register(PlayerGotNote.class, new Capability.IStorage<PlayerGotNote>() {
//
//            @Override
//            public NBTBase writeNBT(Capability<PlayerGotNote> capability, PlayerGotNote instance, EnumFacing side) {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public void readNBT(Capability<PlayerGotNote> capability, PlayerGotNote instance, EnumFacing side, NBTBase nbt) {
//                throw new UnsupportedOperationException();
//            }
//
//        }, () -> {
//            throw new UnsupportedOperationException();
//        });
    }


    private void readMainConfig() {
        // @todo fabric
//        Configuration cfg = TheOneProbe.config;
//        try {
//            cfg.load();
//            cfg.addCustomCategoryComment(Config.CATEGORY_THEONEPROBE, "The One Probe configuration");
//            cfg.addCustomCategoryComment(Config.CATEGORY_PROVIDERS, "Provider configuration");
//            cfg.addCustomCategoryComment(Config.CATEGORY_CLIENT, "Client-side settings");
//            Config.init(cfg);
//        } catch (Exception e1) {
//            TheOneProbe.logger.log(Level.ERROR, "Problem loading config file!", e1);
//        } finally {
//            if (TheOneProbe.config.hasChanged()) {
//                TheOneProbe.config.save();
//            }
//        }
    }

    public void init() {
//        NetworkRegistry.INSTANCE.registerGuiHandler(TheOneProbe.instance, new GuiProxy());
    }

    public void postInit() {
        configureProviders();
        configureEntityProviders();

        // @todo config
//        if (TheOneProbe.config.hasChanged()) {
//            TheOneProbe.config.save();
//        }
    }

    private void configureProviders() {
        List<IProbeInfoProvider> providers = TheOneProbe.theOneProbeImp.getProviders();
        String[] defaultValues = new String[providers.size()];
        int i = 0;
        for (IProbeInfoProvider provider : providers) {
            defaultValues[i++] = provider.getID();
        }

        // @todo fabric
//        String[] sortedProviders = TheOneProbe.config.getStringList("sortedProviders", Config.CATEGORY_PROVIDERS, defaultValues, "Order in which providers should be used");
//        String[] excludedProviders = TheOneProbe.config.getStringList("excludedProviders", Config.CATEGORY_PROVIDERS, new String[] {}, "Providers that should be excluded");
//        Set<String> excluded = new HashSet<>();
//        Collections.addAll(excluded, excludedProviders);
//
//        TheOneProbe.theOneProbeImp.configureProviders(sortedProviders, excluded);
    }

    private void configureEntityProviders() {
        List<IProbeInfoEntityProvider> providers = TheOneProbe.theOneProbeImp.getEntityProviders();
        String[] defaultValues = new String[providers.size()];
        int i = 0;
        for (IProbeInfoEntityProvider provider : providers) {
            defaultValues[i++] = provider.getID();
        }

        // @todo fabric
//        String[] sortedProviders = TheOneProbe.config.getStringList("sortedEntityProviders", Config.CATEGORY_PROVIDERS, defaultValues, "Order in which entity providers should be used");
//        String[] excludedProviders = TheOneProbe.config.getStringList("excludedEntityProviders", Config.CATEGORY_PROVIDERS, new String[] {}, "Entity providers that should be excluded");
//        Set<String> excluded = new HashSet<>();
//        Collections.addAll(excluded, excludedProviders);
//
//        TheOneProbe.theOneProbeImp.configureEntityProviders(sortedProviders, excluded);
    }
}
