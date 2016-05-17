package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
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
    }

    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent event) {
        if (event.isCanceled() || event.getType() != RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            return;
        }
        if (Config.needsProbe) {
            ItemStack mainHeldItem = Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND);
            ItemStack offHeldItem = Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.OFF_HAND);
            if ((mainHeldItem != null && mainHeldItem.getItem() == ModItems.probe) ||
                    (offHeldItem != null && offHeldItem.getItem() == ModItems.probe)) {
                OverlayRenderer.renderHUD();
            }
        } else {
            OverlayRenderer.renderHUD();
        }
    }


    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
}
