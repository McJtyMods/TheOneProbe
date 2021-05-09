package mcjty.theoneprobe.apiimpl.elements;

import java.text.DecimalFormat;

import com.mojang.blaze3d.matrix.MatrixStack;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ElementProgress implements IElement {

    private final long current;
    private final long max;
    private final IProgressStyle style;

    public ElementProgress(long current, long max, IProgressStyle style) {
        this.current = current;
        this.max = max;
        this.style = style;
    }

    public ElementProgress(PacketBuffer buf) {
        current = buf.readLong();
        max = buf.readLong();
        style = new ProgressStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .prefix(buf.readTextComponent())
                .suffix(buf.readTextComponent())
                .borderColor(buf.readInt())
                .filledColor(buf.readInt())
                .alternateFilledColor(buf.readInt())
                .backgroundColor(buf.readInt())
                .showText(buf.readBoolean())
                .numberFormat(NumberFormat.values()[buf.readByte()])
                .lifeBar(buf.readBoolean())
                .armorBar(buf.readBoolean())
                .alignment(buf.readEnumValue(ElementAlignment.class));
    }

    private static DecimalFormat dfCommas = new DecimalFormat("###,###");

    /**
     * If the suffix starts with 'm' we can possibly drop that
     */
	@OnlyIn(Dist.CLIENT)
	public static ITextComponent format(long in, NumberFormat style, ITextComponent suffix) {
		switch (style) {
			case FULL:
				return new StringTextComponent(Long.toString(in)).appendSibling(suffix);
			case COMPACT:
				if (in < 1000) return new StringTextComponent(Long.toString(in) + " ").appendSibling(suffix);
				int unit = 1000;
				int exp = (int) (Math.log(in) / Math.log(unit));
				String s = suffix.getString();
				if (s.startsWith("m")) {
					s = s.substring(1);
					if (exp - 2 >= 0) {
						char pre = "kMGTPE".charAt(exp - 2);
						return new StringTextComponent(String.format("%.1f %s", new Object[]{Double.valueOf(in / Math.pow(unit, exp)), Character.valueOf(pre)})).appendString(s);
					}
					return new StringTextComponent(String.format("%.1f", new Object[]{Double.valueOf(in / Math.pow(unit, exp))})).appendString(s);
				}
				char pre = "kMGTPE".charAt(exp - 1);
				return new StringTextComponent(String.format("%.1f %s", new Object[]{Double.valueOf(in / Math.pow(unit, exp)), Character.valueOf(pre)})).appendSibling(suffix);
			case COMMAS:
				return new StringTextComponent(dfCommas.format(in)).appendSibling(suffix);
			case NONE: return suffix;
		}
		return new StringTextComponent(Long.toString(in));
	}

    @Override
    public void render(MatrixStack matrixStack, int x, int y) {
        ElementProgressRender.render(style, current, max, matrixStack, x, y, getWidth(), getHeight());
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
    public void toBytes(PacketBuffer buf) {
        buf.writeLong(current);
        buf.writeLong(max);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeTextComponent(style.getPrefixComp());
        buf.writeTextComponent(style.getSuffixComp());
        buf.writeInt(style.getBorderColor());
        buf.writeInt(style.getFilledColor());
        buf.writeInt(style.getAlternatefilledColor());
        buf.writeInt(style.getBackgroundColor());
        buf.writeBoolean(style.isShowText());
        buf.writeByte(style.getNumberFormat().ordinal());
        buf.writeBoolean(style.isLifeBar());
        buf.writeBoolean(style.isArmorBar());
        buf.writeEnumValue(style.getAlignment());
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_PROGRESS;
    }
}
