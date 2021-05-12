package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public class ProbeInfo extends ElementVertical {

    public void fromBytes(PacketBuffer buf) {
        children = createElements(buf);
    }

    public ProbeInfo() {
    	super(new LayoutStyle().spacing(2).alignment(ElementAlignment.ALIGN_TOPLEFT));
    }

    public static List<IElement> createElements(PacketBuffer buf) {
        int size = buf.readVarInt();
        List<IElement> elements = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++) {
            int id = buf.readVarInt();
            IElementFactory factory = TheOneProbe.theOneProbeImp.getElementFactory(id);
            elements.add(factory.createElement(buf));
        }
        return elements;
    }

    public static void writeElements(List<IElement> elements, PacketBuffer buf) {
        buf.writeVarInt(elements.size());
        for (IElement element : elements) {
            buf.writeVarInt(element.getID());
            element.toBytes(buf);
        }
    }

    public void removeElement(IElement element) {
        this.getElements().remove(element);
    }
}
