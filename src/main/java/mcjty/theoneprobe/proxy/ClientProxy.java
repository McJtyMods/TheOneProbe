package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.keys.KeyBindings;
import mcjty.theoneprobe.keys.KeyInputHandler;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mcjty.theoneprobe.config.Config.*;

public class ClientProxy implements IProxy {

    @Override
    public void setup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        KeyBindings.init();
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
    }

//    @SubscribeEvent
//    public void testCustomRenderer(RenderGameOverlayEvent event) {
//        if (event.isCanceled() || event.getType() != RenderGameOverlayEvent.ElementType.POTION_ICONS) {
//            return;
//        }
//        IOverlayRenderer renderer = TheOneProbe.instance.theOneProbeImp.getOverlayRenderer();
//        IOverlayStyle style = renderer.createDefaultStyle()
//                .location(-1, -1, -1, 20)
//                .borderThickness(0)
//                .boxColor(0x00000000);
//        IProbeInfo probeInfo = renderer.createProbeInfo();
//        IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
//        vertical
//                .horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_BOTTOMRIGHT))
//                .item(new ItemStack(Items.DIAMOND))
//                .text("Extra!")
//                .item(new ItemStack(Items.EMERALD));
//        vertical
//                .horizontal(probeInfo.defaultLayoutStyle().borderColor(0xffffffff))
//                .entity(EntityList.getEntityStringFromClass(EntityCaveSpider.class))
//                .entity(EntityList.getEntityStringFromClass(EntityCow.class))
//                .entity(EntityList.getEntityStringFromClass(EntityWither.class))
//                .entity(EntityList.getEntityStringFromClass(EntityChicken.class))
//                .entity(EntityList.getEntityStringFromClass(EntityEnderman.class))
//                .entity(EntityList.getEntityStringFromClass(EntityHorse.class))
//                .entity(EntityList.getEntityStringFromClass(EntityWolf.class))
//                .entity(EntityList.getEntityStringFromClass(EntityDragon.class))
//                ;
//        vertical
//                .progress(8, 10, probeInfo.defaultProgressStyle().lifeBar(true));
//        renderer.render(style, probeInfo);
//    }

    public static boolean ignoreNextGuiClose = false;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (ignoreNextGuiClose) {
            Screen current = Minecraft.getInstance().currentScreen;
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

        if (Config.holdKeyToMakeVisible.get()) {
            if (!KeyBindings.toggleVisible.isKeyDown()) {
                return;
            }
        } else {
            if (!Config.isVisible.get()) {
                return;
            }
        }

        if (hasItemInEitherHand(ModItems.creativeProbe)) {
            OverlayRenderer.renderHUD(ProbeMode.DEBUG, event.getMatrixStack(), event.getPartialTicks());
        } else {
            switch (Config.needsProbe.get()) {
                case PROBE_NOTNEEDED:
                case PROBE_NEEDEDFOREXTENDED:
                    OverlayRenderer.renderHUD(getModeForPlayer(), event.getMatrixStack(), event.getPartialTicks());
                    break;
                case PROBE_NEEDED:
                case PROBE_NEEDEDHARD:
                    if (ModItems.hasAProbeSomewhere(Minecraft.getInstance().player)) {
                        OverlayRenderer.renderHUD(getModeForPlayer(), event.getMatrixStack(), event.getPartialTicks());
                    }
                    break;
            }
        }
    }

    private ProbeMode getModeForPlayer() {
        PlayerEntity player = Minecraft.getInstance().player;
        if (Config.extendedInMain.get()) {
            if (hasItemInMainHand(ModItems.probe)) {
                return ProbeMode.EXTENDED;
            }
        }
        return player.isSneaking() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
    }

    private boolean hasItemInEitherHand(Item item) {
        ItemStack mainHeldItem = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND);
        ItemStack offHeldItem = Minecraft.getInstance().player.getHeldItem(Hand.OFF_HAND);
        return mainHeldItem.getItem() == item || offHeldItem.getItem() == item;
    }


    private boolean hasItemInMainHand(Item item) {
        ItemStack mainHeldItem = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND);
        return mainHeldItem.getItem() == item;
    }
}
