package mcjty.theoneprobe.apiimpl;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;

import java.util.ArrayList;
import java.util.List;

public class ProbeInfo extends ElementVertical {

    public List<IElement> getElements() {
        return children;
    }

    public void fromBytes(ByteBuf buf) {
        children = createElements(buf);
    }

    public ProbeInfo() {
        super((Integer) null, 2, ElementAlignment.ALIGN_TOPLEFT);
    }

    public static List<IElement> createElements(ByteBuf buf) {
        int size = buf.readShort();
        List<IElement> elements = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++) {
            int id = buf.readInt();
            IElementFactory factory = TheOneProbe.theOneProbeImp.getElementFactory(id);
            IElement element = factory.createElement(buf);
            elements.add(element);
        }
        return elements;
    }

    public static void writeElements(List<IElement> elements, ByteBuf buf) {
        buf.writeShort(elements.size());
        for (IElement element : elements) {
            buf.writeInt(element.getID());
            element.toBytes(buf);
        }
    }

    public void removeElement(IElement element) {
        this.getElements().remove(element);
    }
}
