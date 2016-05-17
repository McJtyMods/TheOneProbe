package mcjty.theoneprobe.apiimpl;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProgressStyle;
import mcjty.theoneprobe.apiimpl.elements.*;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ProbeInfo implements IProbeInfo {

    private List<Element> elements = new ArrayList<>();

    public List<Element> getElements() {
        return elements;
    }


    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        elements.clear();
        for (int i = 0 ; i < size ; i++) {
            short t = buf.readShort();
            ElementType type = ElementType.values()[t];
            Element element;
            switch (type) {
                case TEXT:
                    element = new ElementText(buf);
                    break;
                case ITEMSTACK:
                    element = new ElementItemStack(buf);
                    break;
                case OFFSET:
                    element = new ElementOffset(buf);
                    break;
                case PROGRESS:
                    element = new ElementProgress(buf);
                    break;
                case NEWLINE:
                    element = new ElementNewline();
                    break;
                default:
                    throw new RuntimeException("Missing type!");
            }
            elements.add(element);
        }
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(elements.size());
        for (Element element : elements) {
            buf.writeShort(element.getType().ordinal());
            element.toBytes(buf);
        }
    }

    @Override
    public IProbeInfo text(String text) {
        elements.add(new ElementText(text));
        return this;
    }

    @Override
    public IProbeInfo item(ItemStack stack) {
        elements.add(new ElementItemStack(stack));
        return this;
    }

    @Override
    public IProbeInfo progress(int current, int max, String prefix, String suffix, ProgressStyle style) {
        elements.add(new ElementProgress(current, max, suffix, prefix, style));
        return this;
    }

    @Override
    public IProbeInfo newline() {
        elements.add(new ElementNewline());
        return this;
    }

    @Override
    public IProbeInfo offset(int dx, int dy) {
        elements.add(new ElementOffset(dx, dy));
        return this;
    }
}
