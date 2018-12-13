package mcjty.theoneprobe.apiimpl.client;

import com.mojang.blaze3d.platform.GlStateManager;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextFormat;
import net.minecraft.util.Identifier;

public class ElementProgressRender {

    private static final Identifier ICONS = new Identifier("textures/gui/icons.png");

    public static void render(IProgressStyle style, long current, long max, int x, int y, int w, int h) {
        if (style.isLifeBar()) {
            renderLifeBar(current, x, y, w, h);
        } else if (style.isArmorBar()) {
            renderArmorBar(current, x, y, w, h);
        } else {
            RenderHelper.drawThickBeveledBox(x, y, x + w, y + h, 1, style.getBorderColor(), style.getBorderColor(), style.getBackgroundColor());
            if (current > 0 && max > 0) {
                // Determine the progress bar width, but limit it to the size of the element (minus 2).
                int dx = (int) Math.min((current * (w - 2) / max), w - 2);

                if (style.getFilledColor() == style.getAlternatefilledColor()) {
                    if (dx > 0) {
                        RenderHelper.drawThickBeveledBox(x + 1, y + 1, x + dx + 1, y + h - 1, 1, style.getFilledColor(), style.getFilledColor(), style.getFilledColor());
                    }
                } else {
                    for (int xx = x + 1; xx <= x + dx + 1; xx++) {
                        int color = (xx & 1) == 0 ? style.getFilledColor() : style.getAlternatefilledColor();
                        RenderHelper.drawVerticalLine(xx, y + 1, y + h - 1, color);
                    }
                }
            }
        }

        if (style.isShowText()) {
            RenderHelper.renderText(MinecraftClient.getInstance(), x + 3, y + 2, style.getPrefix() + ElementProgress.format(current, style.getNumberFormat(), style.getSuffix()));
        }
    }

    private static void renderLifeBar(long current, int x, int y, int w, int h) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        MinecraftClient.getInstance().getTextureManager().bindTexture(ICONS);
        if (current * 4 >= w) {
            // Shortened view
            RenderHelper.drawTexturedModalRect(x, y, 52, 0, 9, 9);
            RenderHelper.renderText(MinecraftClient.getInstance(), x + 12, y, TextFormat.WHITE + String.valueOf((current / 2)));
        } else {
            for (int i = 0; i < current / 2; i++) {
                RenderHelper.drawTexturedModalRect(x, y, 52, 0, 9, 9);
                x += 8;
            }
            if (current % 2 != 0) {
                RenderHelper.drawTexturedModalRect(x, y, 61, 0, 9, 9);
            }
        }
    }

    private static void renderArmorBar(long current, int x, int y, int w, int h) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        MinecraftClient.getInstance().getTextureManager().bindTexture(ICONS);
        if (current * 4 >= w) {
            // Shortened view
            RenderHelper.drawTexturedModalRect(x, y, 43, 9, 9, 9);
            RenderHelper.renderText(MinecraftClient.getInstance(), x + 12, y, TextFormat.WHITE + String.valueOf((current / 2)));
        } else {
            for (int i = 0; i < current / 2; i++) {
                RenderHelper.drawTexturedModalRect(x, y, 43, 9, 9, 9);
                x += 8;
            }
            if (current % 2 != 0) {
                RenderHelper.drawTexturedModalRect(x, y, 25, 9, 9, 9);
            }
        }
    }
}
