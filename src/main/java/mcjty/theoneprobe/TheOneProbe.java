package mcjty.theoneprobe;

import com.mojang.serialization.Codec;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import java.util.*;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.TheOneProbePlugin;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.providers.*;
import mcjty.theoneprobe.commands.ModCommands;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.network.FabricNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TheOneProbe implements ModInitializer {
    public static final String MODID = "theoneprobe";

    public static final Logger logger = LogManager.getLogger();

    public static TheOneProbeImp theOneProbeImp = new TheOneProbeImp();

    public static boolean baubles = false;
    public static boolean tesla = false;
    public static boolean redstoneflux = false;

    public static final ResourceLocation HASPROBE = ResourceLocation.fromNamespaceAndPath(MODID, "hasprobe");
    public static final TagKey<Item> HASPROBE_TAG = TagKey.create(Registries.ITEM, HASPROBE);

    public static final AttachmentType<Boolean> PLAYER_GOT_NOTE =
            AttachmentRegistry.create(
                    ResourceLocation.fromNamespaceAndPath(MODID, "playergotnote"),
                    builder -> builder.initializer(() -> false).persistent(Codec.BOOL).copyOnDeath());

    @Override
    public void onInitialize() {
        NeoForgeModConfigEvents.loading(MODID).register(config -> {
            Config.onLoad(config);
            Config.resolveConfigs();
        });
        NeoForgeModConfigEvents.reloading(MODID).register(config -> {
            Config.onReload(config);
            Config.resolveConfigs();
        });
        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        ModItems.init();
        registerItem("probe", ModItems.PROBE);
        registerItem("creativeprobe", ModItems.CREATIVE_PROBE);
        registerItem("probenote", ModItems.PROBE_NOTE);
        registerItem("diamond_helmet_probe", ModItems.DIAMOND_HELMET_PROBE);
        registerItem("gold_helmet_probe", ModItems.GOLD_HELMET_PROBE);
        registerItem("iron_helmet_probe", ModItems.IRON_HELMET_PROBE);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(MODID, "probe"),
                FabricItemGroup.builder()
                        .title(Component.literal("The One Probe"))
                        .icon(() -> new ItemStack(ModItems.PROBE))
                        .displayItems((parameters, output) -> {
                            output.accept(ModItems.PROBE);
                            output.accept(ModItems.CREATIVE_PROBE);
                            output.accept(ModItems.PROBE_NOTE);
                            output.accept(ModItems.DIAMOND_HELMET_PROBE);
                            output.accept(ModItems.GOLD_HELMET_PROBE);
                            output.accept(ModItems.IRON_HELMET_PROBE);
                        }).build());
        TheOneProbeImp.registerElements();
        theOneProbeImp.registerProvider(new DefaultProbeInfoProvider());
        theOneProbeImp.registerProvider(new DebugProbeInfoProvider());
        theOneProbeImp.registerProvider(new BlockProbeInfoProvider());
        theOneProbeImp.registerEntityProvider(new DefaultProbeInfoEntityProvider());
        theOneProbeImp.registerEntityProvider(new DebugProbeInfoEntityProvider());
        theOneProbeImp.registerEntityProvider(new EntityProbeInfoEntityProvider());
        FabricLoader.getInstance().getEntrypoints("theoneprobe", TheOneProbePlugin.class)
                .forEach(plugin -> plugin.register(theOneProbeImp));
        configureProviders();
        configureEntityProviders();
        FabricNetworking.register();
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, access, environment) -> ModCommands.register(dispatcher));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var player = handler.player;
            if (Config.spawnNote.get() && !player.getAttachedOrCreate(PLAYER_GOT_NOTE)
                    && player.getInventory().add(new ItemStack(ModItems.PROBE_NOTE))) {
                player.setAttached(PLAYER_GOT_NOTE, true);
            }
        });
    }

    private static void registerItem(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(MODID, name), item);
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
