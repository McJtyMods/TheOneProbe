package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.keys.KeyBindings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        // @todo fabric
//        ClientCommandHandler.instance.registerCommand(new CommandTopCfg());
//        ClientCommandHandler.instance.registerCommand(new CommandTopNeed());
//        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        KeyBindings.init();
    }

//    @SubscribeEvent
//    public void registerModels(ModelRegistryEvent event) {
//        ModItems.initClient();
//    }

    public static boolean ignoreNextGuiClose = false;

//    @SubscribeEvent
//    public void onGuiOpen(GuiOpenEvent event) {
//        if (ignoreNextGuiClose) {
//            GuiScreen current = MinecraftClient.getInstance().currentScreen;
//            if (event.getGui() == null && (current instanceof GuiConfig || current instanceof GuiNote)) {
//                ignoreNextGuiClose = false;
//                // We don't want our gui to be closed for a new 'null' guil
//                event.setCanceled(true);
//            }
//        }
//    }

//    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
//    public void renderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
//        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
//            return;
//        }
//
//        if (Config.holdKeyToMakeVisible) {
//            if (!KeyBindings.toggleVisible.isKeyDown()) {
//                return;
//            }
//        } else {
//            if (!Config.isVisible) {
//                return;
//            }
//        }
//
//        if (hasItemInEitherHand(ModItems.creativeProbe)) {
//            OverlayRenderer.renderHUD(ProbeMode.DEBUG, event.getPartialTicks());
//        } else {
//            switch (Config.needsProbe) {
//                case PROBE_NOTNEEDED:
//                case PROBE_NEEDEDFOREXTENDED:
//                    OverlayRenderer.renderHUD(getModeForPlayer(), event.getPartialTicks());
//                    break;
//                case PROBE_NEEDED:
//                case PROBE_NEEDEDHARD:
//                    if (ModItems.hasAProbeSomewhere(MinecraftClient.getInstance().player)) {
//                        OverlayRenderer.renderHUD(getModeForPlayer(), event.getPartialTicks());
//                    }
//                    break;
//            }
//        }
//    }

    private ProbeMode getModeForPlayer() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (Config.extendedInMain) {
            if (hasItemInMainHand(ModItems.probe)) {
                return ProbeMode.EXTENDED;
            }
        }
        return player.isSneaking() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
    }

    private boolean hasItemInEitherHand(Item item) {
        ItemStack mainHeldItem = MinecraftClient.getInstance().player.getStackInHand(Hand.MAIN);
        ItemStack offHeldItem = MinecraftClient.getInstance().player.getStackInHand(Hand.OFF);
        return (mainHeldItem != null && mainHeldItem.getItem() == item) ||
                (offHeldItem != null && offHeldItem.getItem() == item);
    }


    private boolean hasItemInMainHand(Item item) {
        ItemStack mainHeldItem = MinecraftClient.getInstance().player.getStackInHand(Hand.MAIN);
        return mainHeldItem != null && mainHeldItem.getItem() == item;
    }


    @Override
    public void postInit() {
        super.postInit();
    }
}
