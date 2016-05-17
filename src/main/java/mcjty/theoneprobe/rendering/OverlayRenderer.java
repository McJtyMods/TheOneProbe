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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class OverlayRenderer {

    public static void renderHUD() {
        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver == null) {
            return;
        }
        BlockPos blockPos = mouseOver.getBlockPos();
        if (blockPos == null) {
            return;
        }
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player.worldObj.isAirBlock(blockPos)) {
            return;
        }

        ProbeInfo probeInfo = getProbeInfo(player.worldObj, blockPos);
        renderElements(probeInfo);
    }

    private static void renderElements(ProbeInfo probeInfo) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

        Cursor cursor = new Cursor(20, 20, 20, 20);

        for (Element element : probeInfo.getElements()) {
            element.render(cursor);
        }
    }

    private static ProbeInfo getProbeInfo(World world, BlockPos blockPos) {
        IBlockState state = world.getBlockState(blockPos);
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();

        List<IProbeInfoProvider> providers = TheOneProbe.theOneProbeImp.getProviders();
        for (IProbeInfoProvider provider : providers) {
            provider.addProbeInfo(probeInfo, world, state, blockPos);
        }
        if (state.getBlock() instanceof IProbeInfoAccessor) {
            IProbeInfoAccessor accessor = (IProbeInfoAccessor) state.getBlock();
            accessor.addProbeInfo(probeInfo, world, state, blockPos);
        }
        return probeInfo;
    }
}
