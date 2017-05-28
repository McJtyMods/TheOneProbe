package mcjty.theoneprobe.proxy;

import mcjty.lib.tools.MinecraftTools;
import mcjty.theoneprobe.api.ProbeChecker;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.commands.CommandTopCfg;
import mcjty.theoneprobe.commands.CommandTopNeed;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.keys.KeyBindings;
import mcjty.theoneprobe.keys.KeyInputHandler;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static mcjty.theoneprobe.config.Config.*;

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
        ClientCommandHandler.instance.registerCommand(new CommandTopNeed());
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        KeyBindings.init();
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

        if (Config.holdKeyToMakeVisible) {
            if (!KeyBindings.toggleVisible.isKeyDown()) {
                return;
            }
        } else {
            if (!Config.isVisible) {
                return;
            }
        }

        if (hasItemInEitherHand(ModItems.creativeProbe)) {
            OverlayRenderer.renderHUD(ProbeMode.DEBUG, event.getPartialTicks());
        } else {
            switch (Config.needsProbe) {
                case PROBE_NOTNEEDED:
                case PROBE_NEEDEDFOREXTENDED:
                    OverlayRenderer.renderHUD(getModeForPlayer(), event.getPartialTicks());
                    break;
                case PROBE_NEEDED:
                case PROBE_NEEDEDHARD:
                    if (ProbeChecker.hasAProbeSomewhere(MinecraftTools.getPlayer(Minecraft.getMinecraft()))) {
                        OverlayRenderer.renderHUD(getModeForPlayer(), event.getPartialTicks());
                    }
                    break;
            }
        }
    }

    private ProbeMode getModeForPlayer() {
        EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
        if (Config.extendedInMain) {
            if (hasItemInMainHand(ModItems.probe)) {
                return ProbeMode.EXTENDED;
            }
        }
        return player.isSneaking() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
    }

    private boolean hasItemInEitherHand(Item item) {
        ItemStack mainHeldItem = MinecraftTools.getPlayer(Minecraft.getMinecraft()).getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offHeldItem = MinecraftTools.getPlayer(Minecraft.getMinecraft()).getHeldItem(EnumHand.OFF_HAND);
        return (mainHeldItem != null && mainHeldItem.getItem() == item) ||
                (offHeldItem != null && offHeldItem.getItem() == item);
    }


    private boolean hasItemInMainHand(Item item) {
        ItemStack mainHeldItem = MinecraftTools.getPlayer(Minecraft.getMinecraft()).getHeldItem(EnumHand.MAIN_HAND);
        return mainHeldItem != null && mainHeldItem.getItem() == item;
    }


    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
}
