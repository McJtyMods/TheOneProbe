package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import mcjty.theoneprobe.network.NetworkTools;

import java.text.DecimalFormat;

public class ElementProgress implements IElement {

    private final long current;
    private final long max;
    private final IProgressStyle style;

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
                .prefix(NetworkTools.readStringUTF8(buf))
                .suffix(NetworkTools.readStringUTF8(buf))
                .borderColor(buf.readInt())
                .filledColor(buf.readInt())
                .alternateFilledColor(buf.readInt())
                .backgroundColor(buf.readInt())
                .showText(buf.readBoolean())
                .numberFormat(NumberFormat.values()[buf.readByte()])
                .lifeBar(buf.readBoolean());
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
            case NONE:
                return "";
        }
        return Long.toString(in);
    }

    @Override
    public void render(int x, int y) {
        ElementProgressRender.render(style, current, max, x, y, getWidth(), getHeight());
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
        NetworkTools.writeStringUTF8(buf, style.getPrefix());
        NetworkTools.writeStringUTF8(buf, style.getSuffix());
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
