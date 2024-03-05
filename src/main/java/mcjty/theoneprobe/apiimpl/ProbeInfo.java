package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public class ProbeInfo extends ElementVertical {

    public ProbeInfo(FriendlyByteBuf buf) {
        super(buf);
    }

    public ProbeInfo() {
        super(new LayoutStyle().spacing(2).alignment(ElementAlignment.ALIGN_TOPLEFT));
    }

    public static List<IElement> createElements(FriendlyByteBuf buf) {
        return buf.readList(buffer -> {
            var id = buffer.readResourceLocation();
            var factory = TheOneProbe.theOneProbeImp.getElementFactory(id);
            return factory.createElement(buffer);
        });
    }

    public static void writeElements(List<IElement> elements, FriendlyByteBuf buf) {
        buf.writeCollection(elements, (buffer, element) -> {
            buffer.writeResourceLocation(element.getID());
            element.toBytes(buffer);
        });
    }

    public void removeElement(IElement element) {
        this.getElements().remove(element);
    }
}
