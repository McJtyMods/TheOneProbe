package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.LayoutStyle;
import mcjty.theoneprobe.api.ProgressStyle;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ElementHorizontal implements IElement, IProbeInfo {

    public static final int SPACING = 5;

    private List<IElement> children = new ArrayList<>();
    private Integer borderColor;
    private int spacing;

    @Override
    public void render(int x, int y) {
        if (borderColor != null) {
            int w = getWidth();
            int h = getHeight();
            RenderHelper.drawHorizontalLine(x, y, x + w - 1, borderColor);
            RenderHelper.drawHorizontalLine(x, y + h - 1, x + w - 1, borderColor);
            RenderHelper.drawVerticalLine(x, y, y + h - 1, borderColor);
            RenderHelper.drawVerticalLine(x + w - 1, y, y + h - 1, borderColor);
            x += 3;
            y += 3;
        }
        for (IElement element : children) {
            element.render(x, y);
            x += element.getWidth() + spacing;
        }
    }

    private int getBorderSpacing() {
        return borderColor == null ? 0 : 6;
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (IElement element : children) {
            w += element.getWidth();
        }
        return w + spacing * (children.size() - 1) + getBorderSpacing();
    }

    @Override
    public int getHeight() {
        int h = 0;
        for (IElement element : children) {
            int hh = element.getHeight();
            if (hh > h) {
                h = hh;
            }
        }
        return h + getBorderSpacing();
    }

    public ElementHorizontal(Integer borderColor, int spacing) {
        this.borderColor = borderColor;
        this.spacing = spacing;
    }

    public ElementHorizontal(ByteBuf buf) {
        children = ProbeInfo.createElements(buf);
        if (buf.readBoolean()) {
            borderColor = buf.readInt();
        }
        spacing = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ProbeInfo.writeElements(children, buf);
        if (borderColor != null) {
            buf.writeBoolean(true);
            buf.writeInt(borderColor);
        } else {
            buf.writeBoolean(false);
        }
        buf.writeShort(spacing);
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_HORIZONTAL;
    }

    @Override
    public IProbeInfo text(String text) {
        children.add(new ElementText(text));
        return this;
    }

    @Override
    public IProbeInfo item(ItemStack stack) {
        children.add(new ElementItemStack(stack));
        return this;
    }

    @Override
    public IProbeInfo progress(int current, int max, ProgressStyle style) {
        children.add(new ElementProgress(current, max, style));
        return this;
    }

    @Override
    public IProbeInfo horizontal(LayoutStyle style) {
        ElementHorizontal e = new ElementHorizontal(style.getBorderColor(), style.getSpacing());
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo horizontal() {
        ElementHorizontal e = new ElementHorizontal((Integer) null, SPACING);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical(LayoutStyle style) {
        ElementVertical e = new ElementVertical(style.getBorderColor(), style.getSpacing());
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical() {
        ElementVertical e = new ElementVertical((Integer) null, ElementVertical.SPACING);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo element(IElement element) {
        children.add(element);
        return this;
    }
}
