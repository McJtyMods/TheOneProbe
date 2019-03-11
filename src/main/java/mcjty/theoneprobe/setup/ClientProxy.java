package mcjty.theoneprobe.setup;

import com.google.common.util.concurrent.ListenableFuture;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.commands.CommandTopCfg;
import mcjty.theoneprobe.commands.CommandTopNeed;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.keys.KeyBindings;
import mcjty.theoneprobe.keys.KeyInputHandler;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.Callable;

import static mcjty.theoneprobe.config.ConfigSetup.*;

public class ClientProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        ClientCommandHandler.instance.registerCommand(new CommandTopCfg());
        ClientCommandHandler.instance.registerCommand(new CommandTopNeed());
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        KeyBindings.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModItems.initClient();
    }

    public static boolean ignoreNextGuiClose = false;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (ignoreNextGuiClose) {
            GuiScreen current = Minecraft.getMinecraft().currentScreen;
            if (event.getGui() == null && (current instanceof GuiConfig || current instanceof GuiNote)) {
                ignoreNextGuiClose = false;
                // We don't want our gui to be closed for a new 'null' guil
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void renderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        if (ConfigSetup.holdKeyToMakeVisible) {
            if (!KeyBindings.toggleVisible.isKeyDown()) {
                return;
            }
        } else {
            if (!ConfigSetup.isVisible) {
                return;
            }
        }

        if (hasItemInEitherHand(ModItems.creativeProbe)) {
            OverlayRenderer.renderHUD(ProbeMode.DEBUG, event.getPartialTicks());
        } else {
            switch (ConfigSetup.needsProbe) {
                case PROBE_NOTNEEDED:
                case PROBE_NEEDEDFOREXTENDED:
                    OverlayRenderer.renderHUD(getModeForPlayer(), event.getPartialTicks());
                    break;
                case PROBE_NEEDED:
                case PROBE_NEEDEDHARD:
                    if (ModItems.hasAProbeSomewhere(Minecraft.getMinecraft().player)) {
                        OverlayRenderer.renderHUD(getModeForPlayer(), event.getPartialTicks());
                    }
                    break;
            }
        }
    }

    private ProbeMode getModeForPlayer() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (ConfigSetup.extendedInMain) {
            if (hasItemInMainHand(ModItems.probe)) {
                return ProbeMode.EXTENDED;
            }
        }
        return player.isSneaking() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
    }

    private boolean hasItemInEitherHand(Item item) {
        ItemStack mainHeldItem = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offHeldItem = Minecraft.getMinecraft().player.getHeldItem(EnumHand.OFF_HAND);
        return (mainHeldItem != null && mainHeldItem.getItem() == item) ||
                (offHeldItem != null && offHeldItem.getItem() == item);
    }


    private boolean hasItemInMainHand(Item item) {
        ItemStack mainHeldItem = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
        return mainHeldItem != null && mainHeldItem.getItem() == item;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public <V> ListenableFuture<V> addScheduledTaskClient(Callable<V> callableToSchedule) {
        return Minecraft.getMinecraft().addScheduledTask(callableToSchedule);
    }

    @Override
    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
        return Minecraft.getMinecraft().addScheduledTask(runnableToSchedule);
    }
}
