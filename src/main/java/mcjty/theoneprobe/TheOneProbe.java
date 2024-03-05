package mcjty.theoneprobe;

import com.mojang.serialization.Codec;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.providers.*;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.network.*;
import mcjty.theoneprobe.rendering.ClientSetup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(TheOneProbe.MODID)
public class TheOneProbe {
    public static final String MODID = "theoneprobe";

    public static final Logger logger = LogManager.getLogger();

    public static TheOneProbeImp theOneProbeImp = new TheOneProbeImp();

    public static boolean baubles = false;
    public static boolean tesla = false;
    public static boolean redstoneflux = false;

    public static final ResourceLocation HASPROBE = new ResourceLocation(MODID, "hasprobe");
    public static final TagKey<Item> HASPROBE_TAG = TagKey.create(Registries.ITEM, HASPROBE);

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MODID);
    public static final Supplier<AttachmentType<Boolean>> ATTACHMENT_TYPE_PLAYER_GOT_NOTE = ATTACHMENT_TYPES.register("playergotnote", () -> AttachmentType.builder(() -> false)
            .serialize(Codec.BOOL)
            .copyOnDeath()
            .build());

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final Supplier<CreativeModeTab> TAB_PROBE = TABS.register("probe", () -> CreativeModeTab.builder()
            .title(Component.literal("The One Probe"))
            .icon(() -> new ItemStack(ModItems.PROBE))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
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
            .build());


    public TheOneProbe(IEventBus bus, Dist dist) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        bus.addListener(this::onRegisterEvent);
        bus.addListener(this::onRegisterPayloadHandler);
        bus.addListener(this::init);
        bus.addListener(Config::onLoad);
        bus.addListener(Config::onReload);

        bus.addListener(this::processIMC);

        TABS.register(bus);
        ATTACHMENT_TYPES.register(bus);

        if (dist.isClient()) {
            bus.addListener(ClientSetup::onClientSetup);
            bus.addListener(ClientSetup::onRegisterKeyMappings);
        }
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

        NeoForge.EVENT_BUS.register(new ForgeEventHandlers());

        registerCapabilities();
        TheOneProbeImp.registerElements();
        TheOneProbe.theOneProbeImp.registerProvider(new DefaultProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new DebugProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new BlockProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DefaultProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DebugProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new EntityProbeInfoEntityProvider());

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

    public void onRegisterPayloadHandler(RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(TheOneProbe.MODID)
                .versioned("1.0")
                .optional();
        registrar.play(PacketGetEntityInfo.ID, PacketGetEntityInfo::read, handler -> handler
                .server(PacketGetEntityInfo::handle));
        registrar.play(PacketReturnEntityInfo.ID, PacketReturnEntityInfo::read, handler -> handler
                .client(PacketReturnEntityInfo::handle));
        registrar.play(PacketGetInfo.ID, PacketGetInfo::read, handler -> handler
                .server(PacketGetInfo::handle));
        registrar.play(PacketOpenGui.ID, PacketOpenGui::read, handler -> handler
                .client(PacketOpenGui::handle));
        registrar.play(PacketReturnInfo.ID, PacketReturnInfo::create, handler -> handler
                .client(PacketReturnInfo::handle));
    }

    public void onRegisterEvent(RegisterEvent event) {
        event.register(Registries.ITEM, helper -> {
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
