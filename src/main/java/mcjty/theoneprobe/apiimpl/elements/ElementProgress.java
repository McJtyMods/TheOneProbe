package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.Cursor;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProgressStyle;
import mcjty.theoneprobe.apiimpl.DefaultProbeInfoProvider;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;

import java.text.DecimalFormat;

public class ElementProgress implements IElement {

    private final int current;
    private final int max;
    private final String prefix;
    private final String suffix;
    private final ProgressStyle style;

    public ElementProgress(int current, int max, String prefix, String suffix, ProgressStyle style) {
        this.current = current;
        this.max = max;
        this.prefix = prefix;
        this.suffix = suffix;
        this.style = style;
    }

    public ElementProgress(ByteBuf buf) {
        current = buf.readInt();
        max = buf.readInt();
        prefix = NetworkTools.readString(buf);
        suffix = NetworkTools.readString(buf);
        style = new ProgressStyle()
            .borderColor(buf.readInt())
            .filledColor(buf.readInt())
            .alternateFilledColor(buf.readInt())
            .backgroundColor(buf.readInt())
            .showText(buf.readBoolean())
            .numberFormat(NumberFormat.values()[buf.readByte()]);
    }

    @Override
    public void render(Cursor cursor) {
        int w = 100;
        int x = cursor.getX();
        int y = cursor.getY();
        RenderHelper.drawThickBeveledBox(x, y, x + w, y + 12, 1, style.getBorderColor(), style.getBorderColor(), style.getBackgroundColor());
        if (current > 0) {
            int dx = current * (w-1) / max;

            if (style.getFilledColor() == style.getAlternatefilledColor()) {
                RenderHelper.drawThickBeveledBox(x + 1, y + 1, x + dx - 1, y + 12 - 1, 1, style.getFilledColor(), style.getFilledColor(), style.getFilledColor());
            } else {
                for (int xx = x + 1; xx <= x + dx - 1; xx++) {
                    int color = (xx & 1) == 0 ? style.getFilledColor() : style.getAlternatefilledColor();
                    RenderHelper.drawVerticalLine(xx, y + 1, y + 12 - 1, color);
                }
            }
        }

        if (style.isShowText()) {
            RenderHelper.renderText(Minecraft.getMinecraft(), cursor.getX() + 3, cursor.getY() + 2, prefix + format(current, style.getNumberFormat()) + suffix);
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
                char pre = "KMGTP".charAt(exp-1);
                return String.format("%.1f %s", in / Math.pow(unit, exp), pre);
            }
            case COMMAS:
                return dfCommas.format(in);
        }
        return Integer.toString(in);
    }


    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(current);
        buf.writeInt(max);
        NetworkTools.writeString(buf, prefix);
        NetworkTools.writeString(buf, suffix);
        buf.writeInt(style.getBorderColor());
        buf.writeInt(style.getFilledColor());
        buf.writeInt(style.getAlternatefilledColor());
        buf.writeInt(style.getBackgroundColor());
        buf.writeBoolean(style.isShowText());
        buf.writeByte(style.getNumberFormat().ordinal());
    }

    @Override
    public int getID() {
        return DefaultProbeInfoProvider.ELEMENT_PROGRESS;
    }
}
