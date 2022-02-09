package mcjty.theoneprobe;

import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.providers.*;
import mcjty.theoneprobe.commands.ModCommands;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.config.ConfigReload;
import mcjty.theoneprobe.items.AddProbeTagRecipe;
import mcjty.theoneprobe.items.AddProbeTagRecipeSerializer;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.network.PacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TheOneProbe implements ModInitializer {
    public static final String MODID = "theoneprobe";

    public static final Logger logger = LogManager.getLogger();

    public static TheOneProbeImp theOneProbeImp = new TheOneProbeImp();

    public static boolean baubles = false;
    public static boolean tesla = false;
    public static boolean redstoneflux = false;

    public static CreativeModeTab tabProbe = FabricItemGroupBuilder.build(new ResourceLocation(MODID, "probe"), () -> new ItemStack(ModItems.PROBE));


    public void onInitialize() {
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        init();
        ModConfigEvent.LOADING.register(modConfig -> {
            Config.onLoad(modConfig);
            ConfigReload.onLoad(modConfig);
        });
        ModConfigEvent.RELOADING.register(modConfig -> {
            Config.onReload(modConfig);
            ConfigReload.onFileChange(modConfig);
        });
        ModItems.init();
        AddProbeTagRecipe.HELMET_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, asResource("probe_helmet"), new AddProbeTagRecipeSerializer());
    }

    private void init() {

        tesla = FabricLoader.getInstance().isModLoaded("tesla");
        if (tesla) {
            logger.log(Level.INFO, "The One Probe Detected TESLA: enabling support");
        }

        redstoneflux = FabricLoader.getInstance().isModLoaded("redstoneflux");
        if (redstoneflux) {
            logger.log(Level.INFO, "The One Probe Detected RedstoneFlux: enabling support");
        }

        baubles = FabricLoader.getInstance().isModLoaded("baubles");
        if (baubles) {
            if (Config.supportBaubles.get()) {
                logger.log(Level.INFO, "The One Probe Detected Baubles: enabling support");
            } else {
                logger.log(Level.INFO, "The One Probe Detected Baubles but support disabled in config");
                baubles = false;
            }
        }

        ServerPlayerEvents.AFTER_RESPAWN.register(ForgeEventHandlers::onPlayerCloned);
        CommandRegistrationCallback.EVENT.register(ModCommands::register);

        registerCapabilities();
        TheOneProbeImp.registerElements();
        TheOneProbe.theOneProbeImp.registerProvider(new DefaultProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new DebugProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerProvider(new BlockProbeInfoProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DefaultProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new DebugProbeInfoEntityProvider());
        TheOneProbe.theOneProbeImp.registerEntityProvider(new EntityProbeInfoEntityProvider());

        PacketHandler.registerMessages();

        configureProviders();
        configureEntityProviders();
    }

//    private void processIMC(final InterModProcessEvent event) {
//        event.getIMCStream().forEach(message -> {
//            if ("getTheOneProbe".equalsIgnoreCase(message.method())) {
//                Supplier<Function<ITheOneProbe, Void>> supplier = message.getMessageSupplier();
//                supplier.get().apply(theOneProbeImp);
//            }
//        });
//    }

    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(TheOneProbe.MODID, "probe_helmet"), new AddProbeTagRecipeSerializer());
    }

    private static void registerCapabilities(){
//        CapabilityManager.INSTANCE.register(PlayerGotNote.class);
    }

    private void configureProviders() {
        List<IProbeInfoProvider> providers = TheOneProbe.theOneProbeImp.getProviders();
        ResourceLocation[] defaultValues = new ResourceLocation[providers.size()];
        int i = 0;
        for (IProbeInfoProvider provider : providers) {
            defaultValues[i++] = provider.getID();
        }

        String[] excludedProviders = new String[] {}; // @todo TheOneProbe.config.getStringList("excludedProviders", Config.CATEGORY_PROVIDERS, new String[] {}, "Providers that should be excluded");
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

        String[] excludedProviders = new String[] {}; // @todo TheOneProbe.config.getStringList("excludedEntityProviders", Config.CATEGORY_PROVIDERS, new String[] {}, "Entity providers that should be excluded");
        Set<String> excluded = new HashSet<>();
        Collections.addAll(excluded, excludedProviders);

        TheOneProbe.theOneProbeImp.configureEntityProviders(defaultValues, excluded);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

}
