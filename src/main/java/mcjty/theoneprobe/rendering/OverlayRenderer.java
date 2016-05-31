package mcjty.theoneprobe.rendering;

import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.network.PacketGetEntityInfo;
import mcjty.theoneprobe.network.PacketGetInfo;
import mcjty.theoneprobe.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OverlayRenderer {

    private static Map<Pair<Integer,BlockPos>, Pair<Long, ProbeInfo>> cachedInfo = new HashMap<>();
    private static Map<UUID, Pair<Long, ProbeInfo>> cachedEntityInfo = new HashMap<>();
    private static long lastCleanupTime = 0;

    // For a short while we keep displaying the last pair if we have no new information
    // to prevent flickering
    private static Pair<Long, ProbeInfo> lastPair;
    private static long lastPairTime = 0;

    public static void registerProbeInfo(int dim, BlockPos pos, ProbeInfo probeInfo) {
        long time = System.currentTimeMillis();
        cachedInfo.put(Pair.of(dim, pos), Pair.of(time, probeInfo));
    }

    public static void registerProbeInfo(UUID uuid, ProbeInfo probeInfo) {
        long time = System.currentTimeMillis();
        cachedEntityInfo.put(uuid, Pair.of(time, probeInfo));
    }

    public static void renderHUD(ProbeMode mode, float partialTicks) {
        float dist = Config.probeDistance;

        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver != null) {
            if (mouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                renderHUDEntity(mode, mouseOver);
                checkCleanup();
                return;
            }
        }

        EntityPlayerSP entity = Minecraft.getMinecraft().thePlayer;
        Vec3d start  = entity.getPositionEyes(partialTicks);
        Vec3d vec31 = entity.getLook(partialTicks);
        Vec3d end = start.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist);

        mouseOver = entity.worldObj.rayTraceBlocks(start, end, Config.showLiquids);
        if (mouseOver == null) {
            return;
        }

        if (mouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            renderHUDBlock(mode, mouseOver);
        }

        checkCleanup();
    }

    private static void checkCleanup() {
        long time = System.currentTimeMillis();
        if (time > lastCleanupTime + 5000) {
            cleanupCachedBlocks(time);
            cleanupCachedEntities(time);
            lastCleanupTime = time;
        }
    }

    private static void renderHUDEntity(ProbeMode mode, RayTraceResult mouseOver) {
        if (mouseOver.entityHit == null) {
            return;
        }
        UUID uuid = mouseOver.entityHit.getPersistentID();

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        long time = System.currentTimeMillis();

        Pair<Long, ProbeInfo> cacheEntry = cachedEntityInfo.get(uuid);
        if (cacheEntry == null) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetEntityInfo(player.worldObj.provider.getDimension(), mode, mouseOver));
            if (lastPair != null && time < lastPairTime + Config.timeout) {
                renderElements(lastPair.getRight(), Config.getDefaultOverlayStyle());
            }
        } else {
            if (time > cacheEntry.getLeft() + Config.timeout) {
                // This info is slightly old. Update it
                PacketHandler.INSTANCE.sendToServer(new PacketGetEntityInfo(player.worldObj.provider.getDimension(), mode, mouseOver));
            }
            renderElements(cacheEntry.getRight(), Config.getDefaultOverlayStyle());
            lastPair = cacheEntry;
            lastPairTime = time;
        }
    }

    private static void renderHUDBlock(ProbeMode mode, RayTraceResult mouseOver) {
        BlockPos blockPos = mouseOver.getBlockPos();
        if (blockPos == null) {
            return;
        }
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player.worldObj.isAirBlock(blockPos)) {
            return;
        }

        long time = System.currentTimeMillis();

        Pair<Long, ProbeInfo> cacheEntry = cachedInfo.get(Pair.of(player.worldObj.provider.getDimension(), blockPos));
        if (cacheEntry == null) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetInfo(player.worldObj.provider.getDimension(), blockPos, mode, mouseOver));
            if (lastPair != null && time < lastPairTime + Config.timeout) {
                renderElements(lastPair.getRight(), Config.getDefaultOverlayStyle());
            }
        } else {
            if (time > cacheEntry.getLeft() + Config.timeout) {
                // This info is slightly old. Update it
                PacketHandler.INSTANCE.sendToServer(new PacketGetInfo(player.worldObj.provider.getDimension(), blockPos, mode, mouseOver));
            }
            renderElements(cacheEntry.getRight(), Config.getDefaultOverlayStyle());
            lastPair = cacheEntry;
            lastPairTime = time;
        }
    }

    public static void renderOverlay(IOverlayStyle style, IProbeInfo probeInfo) {
        renderElements((ProbeInfo) probeInfo, style);
    }

    private static void cleanupCachedBlocks(long time) {
        // It has been a while. Time to clean up unused cached pairs.
        Map<Pair<Integer,BlockPos>, Pair<Long, ProbeInfo>> newCachedInfo = new HashMap<>();
        for (Map.Entry<Pair<Integer, BlockPos>, Pair<Long, ProbeInfo>> entry : cachedInfo.entrySet()) {
            long t = entry.getValue().getLeft();
            if (time < t + Config.timeout + 1000) {
                newCachedInfo.put(entry.getKey(), entry.getValue());
            }
        }
        cachedInfo = newCachedInfo;
    }

    private static void cleanupCachedEntities(long time) {
        // It has been a while. Time to clean up unused cached pairs.
        Map<UUID, Pair<Long, ProbeInfo>> newCachedInfo = new HashMap<>();
        for (Map.Entry<UUID, Pair<Long, ProbeInfo>> entry : cachedEntityInfo.entrySet()) {
            long t = entry.getValue().getLeft();
            if (time < t + Config.timeout + 1000) {
                newCachedInfo.put(entry.getKey(), entry.getValue());
            }
        }
        cachedEntityInfo = newCachedInfo;
    }

    private static void renderElements(ProbeInfo probeInfo, IOverlayStyle style) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

        final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        final int scaledWidth = scaledresolution.getScaledWidth();
        final int scaledHeight = scaledresolution.getScaledHeight();

        int w = probeInfo.getWidth();
        int h = probeInfo.getHeight();

        int thick = style.getBorderThickness();
        int margin = 0;
        if (thick > 0) {
            w += (thick+3) * 2;
            h += (thick+3) * 2;
            margin = thick + 3;
        }

        int x;
        int y;
        if (style.getLeftX() != -1) {
            x = style.getLeftX();
        } else if (style.getRightX() != -1) {
            x = scaledWidth - w - style.getRightX();
        } else {
            x = (scaledWidth - w) / 2;
        }
        if (style.getTopY() != -1) {
            y = style.getTopY();
        } else if (style.getBottomY() != -1) {
            y = scaledHeight - h - style.getBottomY();
        } else {
            y = (scaledHeight - h) / 2;
        }

        if (thick > 0) {
            RenderHelper.drawThickBeveledBox(x, y, x + w-1, y + h-1, thick, style.getBorderColor(), style.getBorderColor(), style.getBoxColor());
        }

        probeInfo.render(x + margin, y + margin);
    }
}
