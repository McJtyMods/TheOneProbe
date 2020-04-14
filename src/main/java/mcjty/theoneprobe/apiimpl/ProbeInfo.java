package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public class ProbeInfo extends ElementVertical {

    public List<IElement> getElements() {
        return children;
    }

    public void fromBytes(PacketBuffer buf) {
        children = createElements(buf);
    }

    public ProbeInfo() {
        super((Integer) null, 2, ElementAlignment.ALIGN_TOPLEFT);
    }

    public static List<IElement> createElements(PacketBuffer buf) {
        int size = buf.readVarInt();
        List<IElement> elements = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++) {
            int id = buf.readVarInt();
            IElementFactory factory = TheOneProbe.theOneProbeImp.getElementFactory(id);
            if (factory instanceof IElementFactoryNew) {
                elements.add(((IElementFactoryNew) factory).createElement(buf));
            } else {
                elements.add(factory.createElement(buf));
            }
        }
        return elements;
    }

    public static void writeElements(List<IElement> elements, PacketBuffer buf) {
        buf.writeVarInt(elements.size());
        for (IElement element : elements) {
            buf.writeVarInt(element.getID());
            if (element instanceof IElementNew) {
                ((IElementNew) element).toBytes(buf);
            } else {
                element.toBytes(buf);
            }
        }
    }

    public void removeElement(IElement element) {
        this.getElements().remove(element);
    }
}
