package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.apiimpl.elements.*;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ProbeInfo implements IProbeInfo {

    private List<Element> elements = new ArrayList<>();

    public List<Element> getElements() {
        return elements;
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
    public IProbeInfo progress(int current, int max, String suffix) {
        elements.add(new ElementProgress(current, max, suffix));
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
