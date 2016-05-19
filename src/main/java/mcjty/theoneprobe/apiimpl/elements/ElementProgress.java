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

import java.text.DecimalFormat;

public class ElementProgress implements IElement {

    private final int current;
    private final int max;
    private final IProgressStyle style;

    public ElementProgress(int current, int max, IProgressStyle style) {
        this.current = current;
        this.max = max;
        this.style = style;
    }

    public ElementProgress(ByteBuf buf) {
        current = buf.readInt();
        max = buf.readInt();
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
                .numberFormat(NumberFormat.values()[buf.readByte()]);
    }

    @Override
    public void render(int x, int y) {
        int w = getWidth();
        RenderHelper.drawThickBeveledBox(x, y, x + w, y + getHeight(), 1, style.getBorderColor(), style.getBorderColor(), style.getBackgroundColor());
        if (current > 0) {
            int dx = current * (w - 2) / max;

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

        if (style.isShowText()) {
            RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 2, style.getPrefix() + format(current, style.getNumberFormat()) + style.getSuffix());
        }
    }

    private static DecimalFormat dfCommas = new DecimalFormat("###,###");

    public static String format(int in, NumberFormat style) {
        switch (style) {
            case FULL:
                return Integer.toString(in);
            case COMPACT: {
                int unit = 1000;
                if (in < unit) {
                    return Integer.toString(in);
                }
                int exp = (int) (Math.log(in) / Math.log(unit));
                char pre = "KMGTP".charAt(exp - 1);
                return String.format("%.1f %s", in / Math.pow(unit, exp), pre);
            }
            case COMMAS:
                return dfCommas.format(in);
        }
        return Integer.toString(in);
    }


    @Override
    public int getWidth() {
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return style.getHeight();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(current);
        buf.writeInt(max);
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
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_PROGRESS;
    }
}
