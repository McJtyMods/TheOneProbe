package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProgressStyle;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ElementHorizontal implements Element, IProbeInfo {

    private final int SPACING = 5;

    private List<Element> children = new ArrayList<>();

    @Override
    public void render(Cursor cursor) {
        int y = cursor.getY();
        for (Element element : children) {
            element.render(cursor);
            cursor.addX(element.getWidth() + SPACING);
            cursor.setY(y);
        }
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (Element element : children) {
            w += element.getWidth();
        }
        return w + SPACING * (children.size() - 1);
    }

    @Override
    public int getHeight() {
        int h = 0;
        for (Element element : children) {
            int hh = element.getHeight();
            if (hh > h) {
                h = hh;
            }
        }
        return h;
    }

    public ElementHorizontal() {
    }

    public ElementHorizontal(ByteBuf buf) {
        children = ProbeInfo.createElements(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ProbeInfo.writeElements(children, buf);
    }

    @Override
    public ElementType getType() {
        return ElementType.HORIZONTAL;
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
    public IProbeInfo horizontal() {
        ElementHorizontal e = new ElementHorizontal();
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical() {
        ElementVertical e = new ElementVertical();
        children.add(e);
        return e;
    }
}
