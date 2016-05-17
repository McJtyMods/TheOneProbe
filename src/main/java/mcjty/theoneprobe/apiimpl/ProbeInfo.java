package mcjty.theoneprobe.apiimpl;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.apiimpl.elements.*;

import java.util.ArrayList;
import java.util.List;

public class ProbeInfo extends ElementVertical {

    public List<Element> getElements() {
        return children;
    }

    public void fromBytes(ByteBuf buf) {
        children = createElements(buf);
    }

    public static List<Element> createElements(ByteBuf buf) {
        int size = buf.readShort();
        List<Element> elements = new ArrayList<>(size);
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
                case PROGRESS:
                    element = new ElementProgress(buf);
                    break;
                case HORIZONTAL:
                    element = new ElementHorizontal(buf);
                    break;
                case VERTICAL:
                    element = new ElementVertical(buf);
                    break;
                default:
                    throw new RuntimeException("Missing type!");
            }
            elements.add(element);
        }
        return elements;
    }

    public static void writeElements(List<Element> elements, ByteBuf buf) {
        buf.writeShort(elements.size());
        for (Element element : elements) {
            buf.writeShort(element.getType().ordinal());
            element.toBytes(buf);
        }
    }
}
