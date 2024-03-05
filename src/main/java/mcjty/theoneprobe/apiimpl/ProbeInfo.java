package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ProbeInfo extends ElementVertical {

    public ProbeInfo(FriendlyByteBuf buf) {
        super(buf);
    }

    public ProbeInfo() {
    	super(new LayoutStyle().spacing(2).alignment(ElementAlignment.ALIGN_TOPLEFT));
    }

    public static List<IElement> createElements(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<IElement> elements = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++) {
            ResourceLocation id = buf.readResourceLocation();
            IElementFactory factory = TheOneProbe.theOneProbeImp.getElementFactory(id);
            elements.add(factory.createElement(buf));
        }
        return elements;
    }

    public static void writeElements(List<IElement> elements, FriendlyByteBuf buf) {
        buf.writeVarInt(elements.size());
        for (IElement element : elements) {
            buf.writeResourceLocation(element.getID());
            element.toBytes(buf);
        }
    }

    public void removeElement(IElement element) {
        this.getElements().remove(element);
    }
}
