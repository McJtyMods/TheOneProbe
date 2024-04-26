package mcjty.theoneprobe.rendering;

import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.keys.KeyBindings;
import mcjty.theoneprobe.keys.KeyInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

import static mcjty.theoneprobe.config.Config.*;

public class ClientSetup {

    public static void onClientSetup(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new ClientSetup());
        NeoForge.EVENT_BUS.register(new KeyInputHandler());
    }

    public static boolean ignoreNextGuiClose = false;

    @SubscribeEvent
    public void onGuiOpen(ScreenEvent.Opening event) {
        if (ignoreNextGuiClose) {
            Screen current = Minecraft.getInstance().screen;
            if (event.getScreen() == null && (current instanceof GuiConfig || current instanceof GuiNote)) {
                ignoreNextGuiClose = false;
                // We don't want our gui to be closed for a new 'null' guil
                event.setCanceled(true);
            }
        }
    }

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        KeyBindings.init();
        event.register(KeyBindings.toggleVisible);
        event.register(KeyBindings.toggleLiquids);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void renderGameOverlayEvent(RenderGuiLayerEvent.Pre event) {
        if (!event.getName().equals(VanillaGuiLayers.TITLE)) {
            return;
        }
//        if (event.getType() != RenderGuiOverlayEvent.ElementType.TEXT) {
//            return;
//        }

        if (Config.holdKeyToMakeVisible.get()) {
            if (!KeyBindings.toggleVisible.isDown()) {
                return;
            }
        } else {
            if (!Config.isVisible.get()) {
                return;
            }
        }

        if (hasItemInEitherHand(ModItems.CREATIVE_PROBE)) {
            OverlayRenderer.renderHUD(ProbeMode.DEBUG, event.getGuiGraphics(), event.getPartialTick());
        } else {
            switch (Config.needsProbe.get()) {
                case PROBE_NOTNEEDED:
                case PROBE_NEEDEDFOREXTENDED:
                    OverlayRenderer.renderHUD(getModeForPlayer(), event.getGuiGraphics(), event.getPartialTick());
                    break;
                case PROBE_NEEDED:
                case PROBE_NEEDEDHARD:
                    if (ModItems.hasAProbeSomewhere(Minecraft.getInstance().player)) {
                        OverlayRenderer.renderHUD(getModeForPlayer(), event.getGuiGraphics(), event.getPartialTick());
                    }
                    break;
            }
        }
    }

    private ProbeMode getModeForPlayer() {
        Player player = Minecraft.getInstance().player;
        if (Config.extendedInMain.get()) {
            if (hasItemInMainHand(ModItems.PROBE)) {
                return ProbeMode.EXTENDED;
            }
        }
        return player.isShiftKeyDown() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
    }

    private boolean hasItemInEitherHand(Item item) {
        ItemStack mainHeldItem = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHeldItem = Minecraft.getInstance().player.getItemInHand(InteractionHand.OFF_HAND);
        return mainHeldItem.getItem() == item || offHeldItem.getItem() == item;
    }


    private boolean hasItemInMainHand(Item item) {
        ItemStack mainHeldItem = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
        return mainHeldItem.getItem() == item;
    }
}
