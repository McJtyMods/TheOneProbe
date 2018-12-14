package mcjty.theoneprobe.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitData;
import mcjty.theoneprobe.apiimpl.ProbeHitEntityData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.elements.ElementText;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoEntityProvider;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.network.NetworkInit;
import mcjty.theoneprobe.network.PacketGetEntityInfo;
import mcjty.theoneprobe.network.PacketGetInfo;
import mcjty.theoneprobe.network.ThrowableIdentity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextFormat;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.FluidRayTraceMode;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;

public class OverlayRenderer {

    private static Map<Pair<Integer,BlockPos>, Pair<Long, ProbeInfo>> cachedInfo = new HashMap<>();
    private static Map<UUID, Pair<Long, ProbeInfo>> cachedEntityInfo = new HashMap<>();
    private static long lastCleanupTime = 0;

    // For a short while we keep displaying the last pair if we have no new information
    // to prevent flickering
    private static Pair<Long, ProbeInfo> lastPair;
    private static long lastPairTime = 0;

    // When the server delays too long we also show some preliminary information already
    private static long lastRenderedTime = -1;

    public static void registerProbeInfo(int dim, BlockPos pos, ProbeInfo probeInfo) {
        if (probeInfo == null) {
            return;
        }
        long time = System.currentTimeMillis();
        cachedInfo.put(Pair.of(dim, pos), Pair.of(time, probeInfo));
    }

    public static void registerProbeInfo(UUID uuid, ProbeInfo probeInfo) {
        if (probeInfo == null) {
            return;
        }
        long time = System.currentTimeMillis();
        cachedEntityInfo.put(uuid, Pair.of(time, probeInfo));
    }

    public static void renderHUD(ProbeMode mode, float partialTicks) {
        float dist = Config.probeDistance;

        HitResult mouseOver = MinecraftClient.getInstance().hitResult;
        if (mouseOver != null) {
            if (mouseOver.type == HitResult.Type.ENTITY) {
                GlStateManager.pushMatrix();

                double scale = Config.tooltipScale;

                double sw = MinecraftClient.getInstance().window.getScaledWidth();
                double sh = MinecraftClient.getInstance().window.getScaledHeight();
//                ScaledResolution scaledresolution = new ScaledResolution(MinecraftClient.getInstance());
//                double sw = scaledresolution.getScaledWidth_double();
//                double sh = scaledresolution.getScaledHeight_double();

                setupOverlayRendering(sw * scale, sh * scale);
                renderHUDEntity(mode, mouseOver, sw * scale, sh * scale);
                setupOverlayRendering(sw, sh);
                GlStateManager.popMatrix();

                checkCleanup();
                return;
            }
        }

        ClientPlayerEntity entity = MinecraftClient.getInstance().player;
        Vec3d start  = entity.getCameraPosVec(partialTicks);
        Vec3d vec31 = entity.getRotationVec(partialTicks);
        Vec3d end = start.add(vec31.x * dist, vec31.y * dist, vec31.z * dist);

//        mouseOver = entity.getEntityWorld().rayTrace(start, end, Config.showLiquids);
        mouseOver = entity.getEntityWorld().rayTrace(start, end, FluidRayTraceMode.ALWAYS);
        if (mouseOver == null) {
            return;
        }

        if (mouseOver.type == HitResult.Type.BLOCK) {
            GlStateManager.pushMatrix();

            double scale = Config.tooltipScale;

            double sw = MinecraftClient.getInstance().window.getScaledWidth();
            double sh = MinecraftClient.getInstance().window.getScaledHeight();

            setupOverlayRendering(sw * scale, sh * scale);
            renderHUDBlock(mode, mouseOver, sw * scale, sh * scale);
            setupOverlayRendering(sw, sh);

            GlStateManager.popMatrix();
        }

        checkCleanup();
    }

    public static void setupOverlayRendering(double sw, double sh) {
        GlStateManager.clear(256, false);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, sw, sh, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
    }

    private static void checkCleanup() {
        long time = System.currentTimeMillis();
        if (time > lastCleanupTime + 5000) {
            cleanupCachedBlocks(time);
            cleanupCachedEntities(time);
            lastCleanupTime = time;
        }
    }

    private static void renderHUDEntity(ProbeMode mode, HitResult mouseOver, double sw, double sh) {
        Entity entity = mouseOver.entity;
        if (entity == null) {
            return;
        }

        // @todo fabric
//        String entityString = EntityList.getEntityString(entity);
        String entityString = entity.getEntityName();
        if (entityString == null && !(entity instanceof PlayerEntity)) {
            // We can't show info for this entity
            return;
        }

        UUID uuid = entity.getUuid();

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        long time = System.currentTimeMillis();

        Pair<Long, ProbeInfo> cacheEntry = cachedEntityInfo.get(uuid);
        if (cacheEntry == null || cacheEntry.getValue() == null) {

            // To make sure we don't ask it too many times before the server got a chance to send the answer
            // we insert a dummy entry here for a while
            if (cacheEntry == null || time >= cacheEntry.getLeft()) {
                cachedEntityInfo.put(uuid, Pair.of(time + 500, null));
                requestEntityInfo(mode, mouseOver, entity, player);
            }

            if (lastPair != null && time < lastPairTime + Config.timeout) {
                renderElements(lastPair.getRight(), Config.getDefaultOverlayStyle(), sw, sh, null);
                lastRenderedTime = time;
            } else if (Config.waitingForServerTimeout > 0 && lastRenderedTime != -1 && time > lastRenderedTime + Config.waitingForServerTimeout) {
                // It has been a while. Show some info on client that we are
                // waiting for server information
                ProbeInfo info = getWaitingEntityInfo(mode, mouseOver, entity, player);
                registerProbeInfo(uuid, info);
                lastPair = Pair.of(time, info);
                lastPairTime = time;
                renderElements(lastPair.getRight(), Config.getDefaultOverlayStyle(), sw, sh, null);
                lastRenderedTime = time;
            }
        } else {
            if (time > cacheEntry.getLeft() + Config.timeout) {
                // This info is slightly old. Update it

                // To make sure we don't ask it too many times before the server got a chance to send the answer
                // we increase the time a bit here
                cachedEntityInfo.put(uuid, Pair.of(time + 500, cacheEntry.getRight()));
                requestEntityInfo(mode, mouseOver, entity, player);
            }
            renderElements(cacheEntry.getRight(), Config.getDefaultOverlayStyle(), sw, sh, null);
            lastRenderedTime = time;
            lastPair = cacheEntry;
            lastPairTime = time;
        }
    }

    private static void requestEntityInfo(ProbeMode mode, HitResult mouseOver, Entity entity, ClientPlayerEntity player) {
        NetworkInit.getEntityInfo(new PacketGetEntityInfo(player.getEntityWorld().dimension.getType().getRawId(), mode, mouseOver, entity));
    }

    private static void renderHUDBlock(ProbeMode mode, HitResult mouseOver, double sw, double sh) {
        BlockPos blockPos = mouseOver.getBlockPos();
        if (blockPos == null) {
            return;
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player.getEntityWorld().isAir(blockPos)) {
            return;
        }

        long time = System.currentTimeMillis();

        IElement damageElement = null;
        if (Config.showBreakProgress > 0) {
            // @todo fabric
//            float damage = MinecraftClient.getInstance().interactionManager.curBlockDamageMP;
            float damage = 0;   // @todo fabric
            if (damage > 0) {
                if (Config.showBreakProgress == 2) {
                    damageElement = new ElementText("" + TextFormat.RED + "Progress " + (int) (damage * 100) + "%");
                } else {
                    damageElement = new ElementProgress((long) (damage * 100), 100, new ProgressStyle()
                            .prefix("Progress ")
                            .suffix("%")
                            .width(85)
                            .borderColor(0)
                            .filledColor(0)
                            .filledColor(0xff990000)
                            .alternateFilledColor(0xff550000));
                }
            }
        }

        int dimension = player.getEntityWorld().dimension.getType().getRawId(); // @todo fabric: this may not be right!
        Pair<Integer, BlockPos> key = Pair.of(dimension, blockPos);
        Pair<Long, ProbeInfo> cacheEntry = cachedInfo.get(key);
        if (cacheEntry == null || cacheEntry.getValue() == null) {

            // To make sure we don't ask it too many times before the server got a chance to send the answer
            // we insert a dummy entry here for a while
            if (cacheEntry == null || time >= cacheEntry.getLeft()) {
                cachedInfo.put(key, Pair.of(time + 500, null));
                requestBlockInfo(mode, mouseOver, blockPos, player);
            }

            if (lastPair != null && time < lastPairTime + Config.timeout) {
                renderElements(lastPair.getRight(), Config.getDefaultOverlayStyle(), sw, sh, damageElement);
                lastRenderedTime = time;
            } else if (Config.waitingForServerTimeout > 0 && lastRenderedTime != -1 && time > lastRenderedTime + Config.waitingForServerTimeout) {
                // It has been a while. Show some info on client that we are
                // waiting for server information
                ProbeInfo info = getWaitingInfo(mode, mouseOver, blockPos, player);
                registerProbeInfo(dimension, blockPos, info);
                lastPair = Pair.of(time, info);
                lastPairTime = time;
                renderElements(lastPair.getRight(), Config.getDefaultOverlayStyle(), sw, sh, damageElement);
                lastRenderedTime = time;
            }
        } else {
            if (time > cacheEntry.getLeft() + Config.timeout) {
                // This info is slightly old. Update it

                // To make sure we don't ask it too many times before the server got a chance to send the answer
                // we increase the time a bit here
                cachedInfo.put(key, Pair.of(time + 500, cacheEntry.getRight()));
                requestBlockInfo(mode, mouseOver, blockPos, player);
            }
            renderElements(cacheEntry.getRight(), Config.getDefaultOverlayStyle(), sw, sh, damageElement);
            lastRenderedTime = time;
            lastPair = cacheEntry;
            lastPairTime = time;
        }
    }

    // Information for when the server is laggy
    private static ProbeInfo getWaitingInfo(ProbeMode mode, HitResult mouseOver, BlockPos blockPos, ClientPlayerEntity player) {
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();

        World world = player.getEntityWorld();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ItemStack pickBlock = block.getPickStack(world, blockPos, blockState);
        IProbeHitData data = new ProbeHitData(blockPos, mouseOver.pos, mouseOver.side, pickBlock);

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        try {
            DefaultProbeInfoProvider.showStandardBlockInfo(probeConfig, mode, probeInfo, blockState, block, world, blockPos, player, data);
        } catch (Exception e) {
            ThrowableIdentity.registerThrowable(e);
            probeInfo.text(ERROR + "Error (see log for details)!");
        }

        probeInfo.text(ERROR + "Waiting for server...");
        return probeInfo;
    }

    private static ProbeInfo getWaitingEntityInfo(ProbeMode mode, HitResult mouseOver, Entity entity, ClientPlayerEntity player) {
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        IProbeHitEntityData data = new ProbeHitEntityData(mouseOver.pos);

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        try {
            DefaultProbeInfoEntityProvider.showStandardInfo(mode, probeInfo, entity, probeConfig);
        } catch (Exception e) {
            ThrowableIdentity.registerThrowable(e);
            probeInfo.text(ERROR + "Error (see log for details)!");
        }

        probeInfo.text(ERROR + "Waiting for server...");
        return probeInfo;
    }

    private static void requestBlockInfo(ProbeMode mode, HitResult mouseOver, BlockPos blockPos, ClientPlayerEntity player) {
        World world = player.getEntityWorld();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ItemStack pickBlock = block.getPickStack(world, blockPos, blockState);
//        ItemStack pickBlock = block.getPickBlock(blockState, mouseOver, world, blockPos, player); // @todo fabric
        if (pickBlock == null || (!pickBlock.isEmpty() && pickBlock.getItem() == null)) {
            // Protection for some invalid items.
            pickBlock = ItemStack.EMPTY;
        }
        if (pickBlock != null && (!pickBlock.isEmpty()) && Config.getDontSendNBTSet().contains(Registry.ITEM.getId(pickBlock.getItem()))) {
            pickBlock = pickBlock.copy();
            pickBlock.setTag(null);
        }
        NetworkInit.getInfo(new PacketGetInfo(world.dimension.getType().getRawId(), blockPos, mode, mouseOver, pickBlock));
    }

    public static void renderOverlay(IOverlayStyle style, IProbeInfo probeInfo) {
        GlStateManager.pushMatrix();

        double scale = Config.tooltipScale;

        MinecraftClient minecraft = MinecraftClient.getInstance();
        double sw = minecraft.window.getScaledWidth();
        double sh = minecraft.window.getScaledHeight();

        setupOverlayRendering(sw * scale, sh * scale);
        renderElements((ProbeInfo) probeInfo, style, sw * scale, sh * scale, null);
        setupOverlayRendering(sw, sh);
        GlStateManager.popMatrix();
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

    public static void renderElements(ProbeInfo probeInfo, IOverlayStyle style, double sw, double sh,
                                       @Nullable IElement extra) {
        if (extra != null) {
            probeInfo.element(extra);
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

//        final ScaledResolution scaledresolution = new ScaledResolution(MinecraftClient.getInstance());
//        final int scaledWidth = scaledresolution.getScaledWidth();
//        final int scaledHeight = scaledresolution.getScaledHeight();
        int scaledWidth = (int) sw;
        int scaledHeight = (int) sh;

        int w = probeInfo.getWidth();
        int h = probeInfo.getHeight();

        int offset = style.getBorderOffset();
        int thick = style.getBorderThickness();
        int margin = 0;
        if (thick > 0) {
            w += (offset + thick + 3) * 2;
            h += (offset + thick + 3) * 2;
            margin = offset + thick + 3;
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
            if (offset > 0) {
                RenderHelper.drawThickBeveledBox(x, y, x + w-1, y + h-1, thick, style.getBoxColor(), style.getBoxColor(), style.getBoxColor());
            }
            RenderHelper.drawThickBeveledBox(x+offset, y+offset, x + w-1-offset, y + h-1-offset, thick, style.getBorderColor(), style.getBorderColor(), style.getBoxColor());
        }

        // @todo fabric
//        if (!MinecraftClient.getInstance().isGamePaused()) {
//            RenderHelper.rot += .5f;
//        }

        probeInfo.render(x + margin, y + margin);
        if (extra != null) {
            probeInfo.removeElement(extra);
        }
    }
}
