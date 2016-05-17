package mcjty.theoneprobe.rendering;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.elements.Cursor;
import mcjty.theoneprobe.apiimpl.elements.Element;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.RayTraceResult;

import java.util.List;

public class OverlayRenderer {

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

        Cursor cursor = new Cursor(20, 20, 20, 20);

        for (Element element : probeInfo.getElements()) {
            element.render(cursor);
        }
    }
}
