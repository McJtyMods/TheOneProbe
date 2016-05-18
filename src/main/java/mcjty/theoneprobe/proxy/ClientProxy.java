package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.commands.CommandTopCfg;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ModItems.initClient();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandTopCfg());
        Config.initClientConfig().save();
    }

    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent event) {
        if (event.isCanceled() || event.getType() != RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            return;
        }
        if (hasItemInEitherHand(ModItems.creativeProbe)) {
            OverlayRenderer.renderHUD(ProbeMode.DEBUG);
        } else if (Config.needsProbe) {
            if (hasItemInEitherHand(ModItems.probe)) {
                OverlayRenderer.renderHUD(getModeForPlayer());
            }
        } else {
            OverlayRenderer.renderHUD(getModeForPlayer());
        }
    }

    private ProbeMode getModeForPlayer() {
        return Minecraft.getMinecraft().thePlayer.isSneaking() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
    }

    private boolean hasItemInEitherHand(Item item) {
        ItemStack mainHeldItem = Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offHeldItem = Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.OFF_HAND);
        return (mainHeldItem != null && mainHeldItem.getItem() == item) ||
                (offHeldItem != null && offHeldItem.getItem() == item);
    }


    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
}
