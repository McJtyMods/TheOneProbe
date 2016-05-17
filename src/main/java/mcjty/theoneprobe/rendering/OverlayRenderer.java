package mcjty.theoneprobe.rendering;

import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.api.Cursor;
import mcjty.theoneprobe.network.PacketGetInfo;
import mcjty.theoneprobe.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class OverlayRenderer {

    private static Map<Pair<Integer,BlockPos>, Pair<Long, ProbeInfo>> cachedInfo = new HashMap<>();
    private static long lastCleanupTime = 0;

    public static void registerProbeInfo(int dim, BlockPos pos, ProbeInfo probeInfo) {
        long time = System.currentTimeMillis();
        cachedInfo.put(Pair.of(dim, pos), Pair.of(time, probeInfo));
    }

    public static void renderHUD(ProbeMode mode) {
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

        long time = System.currentTimeMillis();

        Pair<Long, ProbeInfo> pair = cachedInfo.get(Pair.of(player.worldObj.provider.getDimension(), blockPos));
        if (pair == null) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetInfo(player.worldObj.provider.getDimension(), blockPos, mode));
        } else {
            if (time > pair.getLeft() + Config.timeout) {
                // This info is slightly old. Update it
                PacketHandler.INSTANCE.sendToServer(new PacketGetInfo(player.worldObj.provider.getDimension(), blockPos, mode));
            }
            renderElements(pair.getRight());
        }

        if (time > lastCleanupTime + 5000) {
            // It has been a while. Time to clean up unused cached pairs.
            Map<Pair<Integer,BlockPos>, Pair<Long, ProbeInfo>> newCachedInfo = new HashMap<>();
            for (Map.Entry<Pair<Integer, BlockPos>, Pair<Long, ProbeInfo>> entry : cachedInfo.entrySet()) {
                long t = entry.getValue().getLeft();
                if (time < t + Config.timeout + 1000) {
                    newCachedInfo.put(entry.getKey(), entry.getValue());
                }
            }
            cachedInfo = newCachedInfo;
            lastCleanupTime = time;
        }
    }

    private static void renderElements(ProbeInfo probeInfo) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

        final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        final int scaledWidth = scaledresolution.getScaledWidth();
        final int scaledHeight = scaledresolution.getScaledHeight();

        int w = probeInfo.getWidth();
        int h = probeInfo.getHeight();

        int thick = Config.boxThickness;
        int margin = 0;
        if (thick > 0) {
            w += (thick+3) * 2;
            h += (thick+3) * 2;
            margin = thick + 3;
        }

        int x;
        int y;
        if (Config.leftX != -1) {
            x = Config.leftX;
        } else if (Config.rightX != -1) {
            x = scaledWidth - w - Config.rightX;
        } else {
            x = (scaledWidth - w) / 2;
        }
        if (Config.topY != -1) {
            y = Config.topY;
        } else if (Config.bottomY != -1) {
            y = scaledHeight - h - Config.bottomY;
        } else {
            y = (scaledHeight - h) / 2;
        }

        if (thick > 0) {
            RenderHelper.drawThickBeveledBox(x, y, x + w-1, y + h-1, thick, Config.boxBorderColor, Config.boxBorderColor, Config.boxFillColor);
        }

        Cursor cursor = new Cursor(x + margin, y + margin);
        probeInfo.render(cursor);
    }
}
