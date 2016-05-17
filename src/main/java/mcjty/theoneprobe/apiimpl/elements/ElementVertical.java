package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProgressStyle;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ElementVertical implements Element, IProbeInfo {

    private final int SPACING = 2;

    protected List<Element> children = new ArrayList<>();

    @Override
    public void render(Cursor cursor) {
        int x = cursor.getX();
        for (Element element : children) {
            element.render(cursor);
            cursor.addY(element.getHeight() + SPACING);
            cursor.setX(x);
        }
    }

    @Override
    public int getHeight() {
        int h = 0;
        for (Element element : children) {
            h += element.getHeight();
        }
        return h + SPACING * (children.size() - 1);
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (Element element : children) {
            int ww = element.getWidth();
            if (ww > w) {
                w = ww;
            }
        }
        return w;
    }

    public ElementVertical() {
    }

    public ElementVertical(ByteBuf buf) {
        children = ProbeInfo.createElements(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ProbeInfo.writeElements(children, buf);
    }

    @Override
    public ElementType getType() {
        return ElementType.VERTICAL;
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
