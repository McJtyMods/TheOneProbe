package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.Cursor;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProgressStyle;
import mcjty.theoneprobe.apiimpl.DefaultProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ElementVertical implements IElement, IProbeInfo {

    public static final int SPACING = 2;

    protected List<IElement> children = new ArrayList<>();
    private Integer borderColor;
    private int spacing = SPACING;

    @Override
    public void render(Cursor cursor) {
        if (borderColor != null) {
            int w = getWidth();
            int h = getHeight();
            RenderHelper.drawHorizontalLine(cursor.getX(), cursor.getY(), cursor.getX() + w - 1, borderColor);
            RenderHelper.drawHorizontalLine(cursor.getX(), cursor.getY() + h - 1, cursor.getX() + w - 1, borderColor);
            RenderHelper.drawVerticalLine(cursor.getX(), cursor.getY(), cursor.getY() + h - 1, borderColor);
            RenderHelper.drawVerticalLine(cursor.getX() + w - 1, cursor.getY(), cursor.getY() + h - 1, borderColor);
            cursor.addX(3);
            cursor.addY(3);
        }
        for (IElement element : children) {
            element.render(cursor.clone());
            cursor.addY(element.getHeight() + spacing);
        }
    }

    private int getBorderSpacing() {
        return borderColor == null ? 0 : 6;
    }

    @Override
    public int getHeight() {
        int h = 0;
        for (IElement element : children) {
            h += element.getHeight();
        }
        return h + spacing * (children.size() - 1) + getBorderSpacing();
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (IElement element : children) {
            int ww = element.getWidth();
            if (ww > w) {
                w = ww;
            }
        }
        return w + getBorderSpacing();
    }

    public ElementVertical(Integer borderColor, int spacing) {
        this.borderColor = borderColor;
        this.spacing = spacing;
    }

    public ElementVertical(ByteBuf buf) {
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
        return DefaultProbeInfoProvider.ELEMENT_VERTICAL;
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
    public IProbeInfo progress(int current, int max, String prefix, String suffix, ProgressStyle style) {
        children.add(new ElementProgress(current, max, prefix, suffix, style));
        return this;
    }

    @Override
    public IProbeInfo horizontal(Integer borderColor, int spacing) {
        ElementHorizontal e = new ElementHorizontal(borderColor, spacing);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo horizontal() {
        ElementHorizontal e = new ElementHorizontal((Integer) null, ElementHorizontal.SPACING);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical(Integer borderColor, int spacing) {
        ElementVertical e = new ElementVertical(borderColor, spacing);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical() {
        ElementVertical e = new ElementVertical((Integer) null, SPACING);
        children.add(e);
        return e;
    }
}
