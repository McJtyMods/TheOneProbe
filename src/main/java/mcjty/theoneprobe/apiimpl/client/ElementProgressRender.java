package mcjty.theoneprobe.apiimpl.client;

import java.util.Objects;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import com.mojang.math.Matrix4f;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.TankReference;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class ElementProgressRender {

    private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");

    public static void render(IProgressStyle style, long current, long max, PoseStack matrixStack, int x, int y, int w, int h) {
        if (style.isLifeBar()) {
            renderLifeBar(current, matrixStack, x, y, w, h);
        } else if (style.isArmorBar()) {
            renderArmorBar(current, matrixStack, x, y, w, h);
        } else {
            RenderHelper.drawThickBeveledBox(matrixStack, x, y, x + w, y + h, 1, style.getBorderColor(), style.getBorderColor(), style.getBackgroundColor());
            if (current > 0 && max > 0) {
                // Determine the progress bar width, but limit it to the size of the element (minus 2).
                int dx = (int) Math.min((current * (w - 2) / max), w - 2);

                if (style.getFilledColor() == style.getAlternatefilledColor()) {
                    if (dx > 0) {
                        RenderHelper.drawThickBeveledBox(matrixStack, x + 1, y + 1, x + dx + 1, y + h - 1, 1, style.getFilledColor(), style.getFilledColor(), style.getFilledColor());
                    }
                } else {
                    for (int xx = x + 1; xx < x + dx + 1; xx++) {
                        int color = (xx & 1) == 0 ? style.getFilledColor() : style.getAlternatefilledColor();
                        RenderHelper.drawVerticalLine(matrixStack, xx, y + 1, y + h - 1, color);
                    }
                }
            }
        }
        renderText(matrixStack, x, y, w, current, style);
    }

    private static void renderText(PoseStack matrixStack, int x, int y, int w, long current, IProgressStyle style) {
        if (style.isShowText()) {
            Minecraft mc = Minecraft.getInstance();
            Font render = mc.font;
            Component s = style.getPrefixComp().copy().append(ElementProgress.format(current, style.getNumberFormat(), style.getSuffixComp()));
            int textWidth = render.width(s.getVisualOrderText());
            switch (style.getAlignment()) {
                case ALIGN_BOTTOMRIGHT -> RenderHelper.renderText(mc, matrixStack, (x + w - 3) - textWidth, y + 2, s);
                case ALIGN_CENTER -> RenderHelper.renderText(mc, matrixStack, (x + (w / 2)) - (textWidth / 2), y + 2, s);
                case ALIGN_TOPLEFT -> RenderHelper.renderText(mc, matrixStack, x + 3, y + 2, s);
            }
        }
    }

    private static void renderLifeBar(long current, PoseStack matrixStack, int x, int y, int w, int h) {

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ICONS);
        Matrix4f matrix = matrixStack.last().pose();
        if (current * 4 >= w) {
            // Shortened view
            RenderHelper.drawTexturedModalRect(matrix, x, y, 52, 0, 9, 9);
            RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x + 12, y, ChatFormatting.WHITE + String.valueOf((current / 2)));
        } else {
            for (int i = 0; i < current / 2; i++) {
                RenderHelper.drawTexturedModalRect(matrix, x, y, 52, 0, 9, 9);
                x += 8;
            }
            if (current % 2 != 0) {
                RenderHelper.drawTexturedModalRect(matrix, x, y, 61, 0, 9, 9);
            }
        }
    }

    private static void renderArmorBar(long current, PoseStack matrixStack, int x, int y, int w, int h) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ICONS);
        Matrix4f matrix = matrixStack.last().pose();
        if (current * 4 >= w) {
            // Shortened view
            RenderHelper.drawTexturedModalRect(matrix, x, y, 43, 9, 9, 9);
            RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x + 12, y, ChatFormatting.WHITE + String.valueOf((current / 2)));
        } else {
            for (int i = 0; i < current / 2; i++) {
                RenderHelper.drawTexturedModalRect(matrix, x, y, 43, 9, 9, 9);
                x += 8;
            }
            if (current % 2 != 0) {
                RenderHelper.drawTexturedModalRect(matrix, x, y, 25, 9, 9, 9);
            }
        }
    }

    public static void renderTank(PoseStack matrixStack, int x, int y, int width, int height, IProgressStyle style, TankReference tank) {
        RenderHelper.drawThickBeveledBox(matrixStack, x, y, x + width, y + height, 1, style.getBorderColor(), style.getBorderColor(), style.getBackgroundColor());
        if (tank.getStored() <= 0) {
            if (style.isShowText()) {
                renderText(matrixStack, x, y, width, 0, style);
            }
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Function<ResourceLocation, TextureAtlasSprite> map = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        width -= 2;
        FluidStack[] fluids = tank.getFluids();
        int start = 1;
        int tanks = fluids.length;
        int max = tank.getCapacity();
        Matrix4f matrix = matrixStack.last().pose();
        for (FluidStack stack : fluids) {
            int lvl = (int) (stack == null ? 0 : (((double) stack.getAmount() / max) * width));
            if (lvl <= 0) {
                continue;
            }
            FluidAttributes attr = stack.getFluid().getAttributes();
            TextureAtlasSprite liquidIcon = map.apply(attr.getStillTexture(stack));
            if (Objects.equals(liquidIcon, map.apply(MissingTextureAtlasSprite.getLocation()))) {
                continue;
            }
            int color = attr.getColor(stack);
            RenderSystem.setShaderColor(((color >> 16) & 255) / 255F, ((color >> 8) & 255) / 255F, (color & 255) / 255F, ((color >> 24) & 255) / 255F);
            while (lvl > 0) {
                int maxX = Math.min(16, lvl);
                lvl -= maxX;
                RenderHelper.drawTexturedModalRect(matrix, x + start, y + 1, liquidIcon, maxX, height - 2);
                start += maxX;
            }
        }
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        if(style.isShowText())
        {
            renderText(matrixStack, x, y, width + 2, tank.getStored(), style);
        }
    }
}
