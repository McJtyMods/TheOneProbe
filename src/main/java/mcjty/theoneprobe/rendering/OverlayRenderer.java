package mcjty.theoneprobe.rendering;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class OverlayRenderer {

    private static int renderItemStack(Minecraft mc, RenderItem itemRender, ItemStack itm, int x, int y, String txt) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        int rc = 0;
        if (itm != null && itm.getItem() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 32.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            short short1 = 240;
            short short2 = 240;
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) short1 / 1.0F, (float) short2 / 1.0F);
            itemRender.renderItemAndEffectIntoGUI(itm, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, itm, x, y, txt);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            rc = 20;
        }

        return rc;
    }

    private static int renderText(Minecraft mc, int x, int y, String txt) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableLighting();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        int width = mc.fontRendererObj.getStringWidth(txt);
        mc.fontRendererObj.drawStringWithShadow(txt, (float)x, (float)y, 16777215);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        // Fixes opaque cooldown overlay a bit lower
        // TODO: check if enabled blending still screws things up down the line.
        GlStateManager.enableBlend();


        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();

        return width;
    }

    public static void renderHUD() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver == null) {
            return;
        }
        if (mouseOver.getBlockPos() == null) {
            return;
        }
        if (player.worldObj.isAirBlock(mouseOver.getBlockPos())) {
            return;
        }

        IBlockState state = player.worldObj.getBlockState(mouseOver.getBlockPos());

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();

        List<IProbeInfoProvider> providers = TheOneProbe.theOneProbeImp.getProviders();
        for (IProbeInfoProvider provider : providers) {
            provider.addProbeInfo(probeInfo, player.worldObj, state, mouseOver.getBlockPos(), player);
        }
        if (state.getBlock() instanceof IProbeInfoAccessor) {
            ((IProbeInfoAccessor) state.getBlock()).addProbeInfo(probeInfo, player.worldObj, state, mouseOver.getBlockPos(), player);
        }

        int x = 20;
        int y = 20;
        int maxh = 10;

        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

        for (ProbeInfo.Element element : probeInfo.getElements()) {
            switch (element.getType()) {
                case TEXT:
                    x += renderText(Minecraft.getMinecraft(), x, y, (String) element.getElement());
                    break;
                case ITEM:
                    x += renderItemStack(Minecraft.getMinecraft(), itemRender, (ItemStack) element.getElement(), x, y, "");
                    maxh = updateMaxH(20, maxh);
                    break;
                case PROGRESS:
                    Pair<Integer,Integer> progressPair = (Pair<Integer,Integer>) element.getElement();
                    int cur = progressPair.getLeft();
                    int max = progressPair.getRight();
                    RenderHelper.drawThickBeveledBox(x, y, x + 100, y + 10, 1, 0xffffffff, 0xffffffff, 0xff000000);
                    RenderHelper.drawThickBeveledBox(x+31, y+1, x + 100 - 1, y + 10 - 1, 1, 0xffff0000, 0xffff0000, 0xffff0000);
                    maxh = updateMaxH(10, maxh);
                    break;
                case NEWLINE:
                    y += maxh;
                    x = 20;
                    maxh = 10;
                    break;
                case OFFSET:
                    Pair<Integer,Integer> offsetPair = (Pair<Integer,Integer>) element.getElement();
                    x += offsetPair.getLeft();
                    y += offsetPair.getRight();
                    break;
            }
        }
    }

    private static int updateMaxH(int h, int maxh) {
        if (h > maxh) {
            return h;
        } else {
            return maxh;
        }
    }
}
