package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.ProgressStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;

public class ElementProgress implements IElement {

    private final long current;
    private final long max;
    private final IProgressStyle style;

    private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");


    public ElementProgress(long current, long max, IProgressStyle style) {
        this.current = current;
        this.max = max;
        this.style = style;
    }

    public ElementProgress(ByteBuf buf) {
        current = buf.readLong();
        max = buf.readLong();
        style = new ProgressStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .prefix(NetworkTools.readString(buf))
                .suffix(NetworkTools.readString(buf))
                .borderColor(buf.readInt())
                .filledColor(buf.readInt())
                .alternateFilledColor(buf.readInt())
                .backgroundColor(buf.readInt())
                .showText(buf.readBoolean())
                .numberFormat(NumberFormat.values()[buf.readByte()])
                .lifeBar(buf.readBoolean());
    }

    @Override
    public void render(int x, int y) {
        if (style.isLifeBar()) {
            renderLifeBar(x, y);
        } else {
            int w = getWidth();
            RenderHelper.drawThickBeveledBox(x, y, x + w, y + getHeight(), 1, style.getBorderColor(), style.getBorderColor(), style.getBackgroundColor());
            if (current > 0) {
                int dx = (int) (current * (w - 2) / max);

                if (style.getFilledColor() == style.getAlternatefilledColor()) {
                    if (dx > 0) {
                        RenderHelper.drawThickBeveledBox(x + 1, y + 1, x + dx + 1, y + getHeight() - 1, 1, style.getFilledColor(), style.getFilledColor(), style.getFilledColor());
                    }
                } else {
                    for (int xx = x + 1; xx <= x + dx + 1; xx++) {
                        int color = (xx & 1) == 0 ? style.getFilledColor() : style.getAlternatefilledColor();
                        RenderHelper.drawVerticalLine(xx, y + 1, y + getHeight() - 1, color);
                    }
                }
            }
        }

        if (style.isShowText()) {
            RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 2, style.getPrefix() + format(current, style.getNumberFormat()) + style.getSuffix());
        }
    }

    private void renderLifeBar(int x, int y) {
        int w = getWidth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);
        if (current * 4 >= w) {
            // Shortened view
            RenderHelper.drawTexturedModalRect(x, y, 52, 0, 9, 9);
            RenderHelper.renderText(Minecraft.getMinecraft(), x + 12, y, TextFormatting.WHITE + String.valueOf((current / 2)));
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

    private static DecimalFormat dfCommas = new DecimalFormat("###,###");

    public static String format(long in, NumberFormat style) {
        switch (style) {
            case FULL:
                return Long.toString(in);
            case COMPACT: {
                int unit = 1000;
                if (in < unit) {
                    return Long.toString(in);
                }
                int exp = (int) (Math.log(in) / Math.log(unit));
                char pre = "KMGTP".charAt(exp - 1);
                return String.format("%.1f %s", in / Math.pow(unit, exp), pre);
            }
            case COMMAS:
                return dfCommas.format(in);
        }
        return Long.toString(in);
    }


    @Override
    public int getWidth() {
        if (style.isLifeBar()) {
            if (current * 4 >= style.getWidth()) {
                return 100;
            } else {
                return (int) (current * 4 + 2);
            }
        }
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return style.getHeight();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(current);
        buf.writeLong(max);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        NetworkTools.writeString(buf, style.getPrefix());
        NetworkTools.writeString(buf, style.getSuffix());
        buf.writeInt(style.getBorderColor());
        buf.writeInt(style.getFilledColor());
        buf.writeInt(style.getAlternatefilledColor());
        buf.writeInt(style.getBackgroundColor());
        buf.writeBoolean(style.isShowText());
        buf.writeByte(style.getNumberFormat().ordinal());
        buf.writeBoolean(style.isLifeBar());
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_PROGRESS;
    }
}
