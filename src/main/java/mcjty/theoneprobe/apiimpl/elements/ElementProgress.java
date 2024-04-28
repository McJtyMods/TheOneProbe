package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;

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

    public ElementProgress(RegistryFriendlyByteBuf buf) {
        current = buf.readLong();
        max = buf.readLong();
        style = new ProgressStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .prefix(ComponentSerialization.STREAM_CODEC.decode(buf))
                .suffix(ComponentSerialization.STREAM_CODEC.decode(buf))
                .borderColor(buf.readInt())
                .filledColor(buf.readInt())
                .alternateFilledColor(buf.readInt())
                .backgroundColor(buf.readInt())
                .showText(buf.readBoolean())
                .numberFormat(NumberFormat.values()[buf.readByte()])
                .lifeBar(buf.readBoolean())
                .armorBar(buf.readBoolean())
                .alignment(buf.readEnum(ElementAlignment.class));
    }
    
    // Helper method that allows to edit the style of a helper method reducing copy/pasting code from internals
    public IProgressStyle getStyle() {
    	return style;
    }
    
    private static DecimalFormat dfCommas = new DecimalFormat("###,###");

    /**
     * If the suffix starts with 'm' we can possibly drop that
     */
	public static Component format(long in, NumberFormat style, Component suffix) {
		switch (style) {
			case FULL:
				return Component.literal(Long.toString(in)).append(suffix);
			case COMPACT:
				if (in < 1000) {
                    return Component.literal(Long.toString(in) + " ").append(suffix);
                }
				int unit = 1000;
				int exp = (int) (Math.log(in) / Math.log(unit));
				String s = suffix.getString();
				if (s.startsWith("m")) {
					s = s.substring(1);
					if (exp - 2 >= 0) {
						char pre = "kMGTPE".charAt(exp - 2);
						return Component.literal(String.format("%.1f %s", Double.valueOf(in / Math.pow(unit, exp)), Character.valueOf(pre))).append(Component.literal(s).withStyle(suffix.getStyle()));
					}
					return Component.literal(String.format("%.1f", Double.valueOf(in / Math.pow(unit, exp)))).append(Component.literal(s).withStyle(suffix.getStyle()));
				}
				char pre = "kMGTPE".charAt(exp - 1);
				return Component.literal(String.format("%.1f %s", Double.valueOf(in / Math.pow(unit, exp)), Character.valueOf(pre))).append(suffix);
			case COMMAS:
				return Component.literal(dfCommas.format(in)).append(suffix);
			case NONE: return suffix;
		}
		return Component.literal(Long.toString(in));
	}

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        ElementProgressRender.render(style, current, max, graphics, x, y, getWidth(), getHeight());
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
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeLong(current);
        buf.writeLong(max);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        ComponentSerialization.STREAM_CODEC.encode(buf, style.getPrefixComp());
        ComponentSerialization.STREAM_CODEC.encode(buf, style.getSuffixComp());
        buf.writeInt(style.getBorderColor());
        buf.writeInt(style.getFilledColor());
        buf.writeInt(style.getAlternatefilledColor());
        buf.writeInt(style.getBackgroundColor());
        buf.writeBoolean(style.isShowText());
        buf.writeByte(style.getNumberFormat().ordinal());
        buf.writeBoolean(style.isLifeBar());
        buf.writeBoolean(style.isArmorBar());
        buf.writeEnum(style.getAlignment());
    }

    @Override
    public ResourceLocation getID() {
        return TheOneProbeImp.ELEMENT_PROGRESS;
    }
}
