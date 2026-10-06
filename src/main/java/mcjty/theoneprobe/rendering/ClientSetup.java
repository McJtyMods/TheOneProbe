package mcjty.theoneprobe.rendering;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import mcjty.theoneprobe.network.PacketOpenGui;
import mcjty.theoneprobe.network.PacketReturnEntityInfo;
import mcjty.theoneprobe.network.PacketReturnInfo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.api.ClientModInitializer;
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

import static mcjty.theoneprobe.config.Config.*;

public class ClientSetup implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindings.init();
        KeyMappingHelper.registerKeyMapping(KeyBindings.toggleVisible);
        KeyMappingHelper.registerKeyMapping(KeyBindings.toggleLiquids);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.gui.screen() == null) new KeyInputHandler().onKeyInput();
        });
        ClientPlayNetworking.registerGlobalReceiver(
                PacketReturnInfo.TYPE, (packet, context) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(
                PacketReturnEntityInfo.TYPE, (packet, context) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(
                PacketOpenGui.TYPE, (packet, context) -> packet.handle());
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.TITLE_AND_SUBTITLE,
                Identifier.fromNamespaceAndPath("theoneprobe", "overlay"), this::renderOverlay);
    }

    private void renderOverlay(GuiGraphicsExtractor graphics, DeltaTracker partialTick) {
        if (Minecraft.getInstance().player == null) return;
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
            OverlayRenderer.renderHUD(ProbeMode.DEBUG, graphics, partialTick.getGameTimeDeltaPartialTick(true));
        } else {
            switch (Config.needsProbe.get()) {
                case PROBE_NOTNEEDED:
                case PROBE_NEEDEDFOREXTENDED:
                    OverlayRenderer.renderHUD(getModeForPlayer(), graphics, partialTick.getGameTimeDeltaPartialTick(true));
                    break;
                case PROBE_NEEDED:
                case PROBE_NEEDEDHARD:
                    if (ModItems.hasAProbeSomewhere(Minecraft.getInstance().player)) {
                        OverlayRenderer.renderHUD(getModeForPlayer(), graphics, partialTick.getGameTimeDeltaPartialTick(true));
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
