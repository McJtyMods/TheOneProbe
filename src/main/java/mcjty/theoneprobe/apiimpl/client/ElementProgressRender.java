package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.TankReference;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Objects;
import java.util.function.Function;

public class ElementProgressRender {

    private static final Identifier HEART_FULL = Identifier.withDefaultNamespace("hud/heart/full");
    private static final Identifier HEART_HALF = Identifier.withDefaultNamespace("hud/heart/half");
    private static final Identifier ARMOR_FULL = Identifier.withDefaultNamespace("hud/armor_full");
    private static final Identifier ARMOR_HALF = Identifier.withDefaultNamespace("hud/armor_half");

    public static void render(IProgressStyle style, long current, long max, GuiGraphics graphics, int x, int y, int w, int h) {
        if (style.isLifeBar()) {
            renderLifeBar(current, graphics, x, y, w, h);
        } else if (style.isArmorBar()) {
            renderArmorBar(current, graphics, x, y, w, h);
        } else {
            RenderHelper.drawThickBeveledBox(graphics, x, y, x + w, y + h, 1, style.getBorderColor(), style.getBorderColor(), style.getBackgroundColor());
            if (current > 0 && max > 0) {
                // Determine the progress bar width, but limit it to the size of the element (minus 2).
                int dx = (int) Math.min((current * (w - 2) / max), w - 2);

                if (style.getFilledColor() == style.getAlternatefilledColor()) {
                    if (dx > 0) {
                        RenderHelper.drawThickBeveledBox(graphics, x + 1, y + 1, x + dx + 1, y + h - 1, 1, style.getFilledColor(), style.getFilledColor(), style.getFilledColor());
                    }
                } else {
                    for (int xx = x + 1; xx < x + dx + 1; xx++) {
                        int color = (xx & 1) == 0 ? style.getFilledColor() : style.getAlternatefilledColor();
                        RenderHelper.drawVerticalLine(graphics, xx, y + 1, y + h - 1, color);
                    }
                }
            }
        }
        renderText(graphics, x, y, w, current, style);
    }

    private static void renderText(GuiGraphics graphics, int x, int y, int w, long current, IProgressStyle style) {
        if (style.isShowText()) {
            Minecraft mc = Minecraft.getInstance();
            Font render = mc.font;
            Component s = style.getPrefixComp().copy().append(ElementProgress.format(current, style.getNumberFormat(), style.getSuffixComp()));
            int textWidth = render.width(s.getVisualOrderText());
            switch (style.getAlignment()) {
                case ALIGN_BOTTOMRIGHT -> RenderHelper.renderText(mc, graphics, (x + w - 3) - textWidth, y + 2, s);
                case ALIGN_CENTER -> RenderHelper.renderText(mc, graphics, (x + (w / 2)) - (textWidth / 2), y + 2, s);
                case ALIGN_TOPLEFT -> RenderHelper.renderText(mc, graphics, x + 3, y + 2, s);
            }
        }
    }

    private static void renderLifeBar(long current, GuiGraphics graphics, int x, int y, int w, int h) {
        if (current * 4 >= w) {
            // Shortened view
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_FULL, x, y, 9, 9);
            RenderHelper.renderText(Minecraft.getInstance(), graphics, x + 12, y, ChatFormatting.WHITE + String.valueOf((current / 2)));
        } else {
            for (int i = 0; i < current / 2; i++) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_FULL, x, y, 9, 9);
                x += 8;
            }
            if (current % 2 != 0) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_HALF, x, y, 9, 9);
            }
        }
    }

    private static void renderArmorBar(long current, GuiGraphics graphics, int x, int y, int w, int h) {
        if (current * 4 >= w) {
            // Shortened view
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL, x, y, 9, 9);
            RenderHelper.renderText(Minecraft.getInstance(), graphics, x + 12, y, ChatFormatting.WHITE + String.valueOf((current / 2)));
        } else {
            for (int i = 0; i < current / 2; i++) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL, x, y, 9, 9);
                x += 8;
            }
            if (current % 2 != 0) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_HALF, x, y, 9, 9);
            }
        }
    }

    public static void renderTank(GuiGraphics graphics, int x, int y, int width, int height, IProgressStyle style, TankReference tank) {
        RenderHelper.drawThickBeveledBox(graphics, x, y, x + width, y + height, 1, style.getBorderColor(), style.getBorderColor(), style.getBackgroundColor());
        if (tank.getStored() <= 0) {
            if (style.isShowText()) {
                renderText(graphics, x, y, width, 0, style);
            }
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Function<Identifier, TextureAtlasSprite> map = mc.getAtlasManager()
                .getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS)::getSprite;
        width -= 2;
        FluidStack[] fluids = tank.getFluids();
        int start = 1;
        int tanks = fluids.length;
        int max = tank.getCapacity();
        for (FluidStack stack : fluids) {
            int lvl = (int) (stack == null ? 0 : (((double) stack.getAmount() / max) * width));
            if (lvl <= 0) {
                continue;
            }
            Identifier stillTexture = IClientFluidTypeExtensions.of(stack.getFluid()).getStillTexture(stack);
            TextureAtlasSprite liquidIcon = map.apply(stillTexture);
            if (Objects.equals(liquidIcon, map.apply(MissingTextureAtlasSprite.getLocation()))) {
                continue;
            }
            int color = IClientFluidTypeExtensions.of(stack.getFluid()).getTintColor(stack);
            while (lvl > 0) {
                int maxX = Math.min(16, lvl);
                lvl -= maxX;
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, liquidIcon, x + start, y + 1, maxX, height - 2, color);
                start += maxX;
            }
        }
        if(style.isShowText()) {
            renderText(graphics, x, y, width + 2, tank.getStored(), style);
        }
    }
}
