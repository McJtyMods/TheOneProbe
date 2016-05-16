package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeInfo;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ProbeInfo implements IProbeInfo {

    private List<Element> elements = new ArrayList<>();

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public IProbeInfo text(String text) {
        elements.add(new Element(ElementType.TEXT, text));
        return this;
    }

    @Override
    public IProbeInfo item(ItemStack stack) {
        elements.add(new Element(ElementType.ITEM, stack));
        return this;
    }

    @Override
    public IProbeInfo progress(int current, int max) {
        elements.add(new Element(ElementType.PROGRESS, Pair.of(current, max)));
        return this;
    }

    @Override
    public IProbeInfo newline() {
        elements.add(new Element(ElementType.NEWLINE, null));
        return this;
    }

    @Override
    public IProbeInfo offset(int dx, int dy) {
        elements.add(new Element(ElementType.OFFSET, Pair.of(dx, dy)));
        return this;
    }

    public static class Element {
        private final ElementType type;
        private final Object element;

        public Element(ElementType type, Object element) {
            this.type = type;
            this.element = element;
        }

        public ElementType getType() {
            return type;
        }

        public Object getElement() {
            return element;
        }
    }

    public enum ElementType {
        TEXT,
        ITEM,
        PROGRESS,
        NEWLINE,
        OFFSET
    }
}
