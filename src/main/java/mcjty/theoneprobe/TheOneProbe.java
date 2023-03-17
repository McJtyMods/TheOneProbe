package mcjty.theoneprobe;

import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.providers.*;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.*;
import mcjty.theoneprobe.network.PacketHandler;
import mcjty.theoneprobe.rendering.ClientSetup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod("theoneprobe")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TheOneProbe {
    public static final String MODID = "theoneprobe";

    public static final Logger logger = LogManager.getLogger();

    public static TheOneProbeImp theOneProbeImp = new TheOneProbeImp();

    public static boolean baubles = false;
    public static boolean tesla = false;
    public static boolean redstoneflux = false;

    public static CreativeModeTab tabProbe;


    public TheOneProbe() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::init);
        bus.addListener(Config::onLoad);
        bus.addListener(Config::onReload);
        bus.addListener(this::registerTabs);

        bus.addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(ClientSetup::onClientSetup);
            bus.addListener(ClientSetup::onRegisterKeyMappings);
        });
    }

    private void registerTabs(CreativeModeTabEvent.Register event) {
        tabProbe = event.registerCreativeModeTab(new ResourceLocation(TheOneProbe.MODID, "probe"), builder -> builder
                .icon(() -> new ItemStack(ModItems.PROBE))
                .displayItems((featureFlags, output) -> {
                    if (ModItems.CREATIVE_PROBE != null) {
                        output.accept(ModItems.CREATIVE_PROBE);
                    }
                    if (ModItems.PROBE != null) {
                        output.accept(ModItems.PROBE);
                    }
                    if (ModItems.DIAMOND_HELMET_PROBE != null) {
                        output.accept(ModItems.DIAMOND_HELMET_PROBE);
                    }
                    if (ModItems.GOLD_HELMET_PROBE != null) {
                        output.accept(ModItems.GOLD_HELMET_PROBE);
                    }
                    if (ModItems.IRON_HELMET_PROBE != null) {
                        output.accept(ModItems.IRON_HELMET_PROBE);
                    }
                    if (ModItems.PROBE_GOGGLES != null) {
                        output.accept(ModItems.PROBE_GOGGLES);
                    }
                    if (ModItems.PROBE_NOTE != null) {
                        output.accept(ModItems.PROBE_NOTE);
                    }
                })
        );
    }


    private void init(final FMLCommonSetupEvent event) {

        tesla = ModList.get().isLoaded("tesla");
        if (tesla) {
            logger.log(Level.INFO, "The One Probe Detected TESLA: enabling support");
        }

        redstoneflux = ModList.get().isLoaded("redstoneflux");
        if (redstoneflux) {
            logger.log(Level.INFO, "The One Probe Detected RedstoneFlux: enabling support");
        }

        baubles = ModList.get().isLoaded("baubles");
        if (baubles) {
            if (Config.supportBaubles.get()) {
                logger.log(Level.INFO, "The One Probe Detected Baubles: enabling support");
            } else {
                logger.log(Level.INFO, "The One Probe Detected Baubles but support disabled in config");
                baubles = false;
            }
        }

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        registerCapabilities();
        TheOneProbeImp.registerElements();
        TheOneProbe.theOneProbeImp.registerProvider(new DefaultProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new DebugProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new BlockProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DefaultProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DebugProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new EntityProbeInfoEntityProvider());

        PacketHandler.registerMessages("theoneprobe");

        configureProviders();
        configureEntityProviders();
    }

    private void processIMC(final InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            if ("getTheOneProbe".equalsIgnoreCase(message.method())) {
                Supplier<Function<ITheOneProbe, Void>> supplier = message.getMessageSupplier();
                supplier.get().apply(theOneProbeImp);
            }
        });
    }

    @SubscribeEvent
    public static void onRegisterEvent(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper -> {
            AddProbeTagRecipe.HELMET_SERIALIZER = new AddProbeTagRecipeSerializer();
            helper.register(new ResourceLocation(TheOneProbe.MODID, "probe_helmet"), AddProbeTagRecipe.HELMET_SERIALIZER);
        });
        event.register(ForgeRegistries.Keys.ITEMS, helper -> {
            ModItems.init();

            helper.register(new ResourceLocation(TheOneProbe.MODID, "probe"), ModItems.PROBE);
            helper.register(new ResourceLocation(TheOneProbe.MODID, "creativeprobe"), ModItems.CREATIVE_PROBE);
            helper.register(new ResourceLocation(TheOneProbe.MODID, "probenote"), ModItems.PROBE_NOTE);

            helper.register(new ResourceLocation(TheOneProbe.MODID, "diamond_helmet_probe"), ModItems.DIAMOND_HELMET_PROBE);
            helper.register(new ResourceLocation(TheOneProbe.MODID, "gold_helmet_probe"), ModItems.GOLD_HELMET_PROBE);
            helper.register(new ResourceLocation(TheOneProbe.MODID, "iron_helmet_probe"), ModItems.IRON_HELMET_PROBE);

//            if (TheOneProbe.baubles) {
//                helper.register(ModItems.PROBE_GOGGLES);
//            }
        });
    }


    private static void registerCapabilities() {
//        CapabilityManager.INSTANCE.register(PlayerGotNote.class);
    }

    private void configureProviders() {
        List<IProbeInfoProvider> providers = TheOneProbe.theOneProbeImp.getProviders();
        ResourceLocation[] defaultValues = new ResourceLocation[providers.size()];
        int i = 0;
        for (IProbeInfoProvider provider : providers) {
            defaultValues[i++] = provider.getID();
        }

        String[] excludedProviders = new String[]{}; // @todo TheOneProbe.config.getStringList("excludedProviders", Config.CATEGORY_PROVIDERS, new String[] {}, "Providers that should be excluded");
        Set<String> excluded = new HashSet<>();
        Collections.addAll(excluded, excludedProviders);

        TheOneProbe.theOneProbeImp.configureProviders(defaultValues, excluded);
    }

    private void configureEntityProviders() {
        List<IProbeInfoEntityProvider> providers = TheOneProbe.theOneProbeImp.getEntityProviders();
        String[] defaultValues = new String[providers.size()];
        int i = 0;
        for (IProbeInfoEntityProvider provider : providers) {
            defaultValues[i++] = provider.getID();
        }

        String[] excludedProviders = new String[]{}; // @todo TheOneProbe.config.getStringList("excludedEntityProviders", Config.CATEGORY_PROVIDERS, new String[] {}, "Entity providers that should be excluded");
        Set<String> excluded = new HashSet<>();
        Collections.addAll(excluded, excludedProviders);

        TheOneProbe.theOneProbeImp.configureEntityProviders(defaultValues, excluded);
    }

}
