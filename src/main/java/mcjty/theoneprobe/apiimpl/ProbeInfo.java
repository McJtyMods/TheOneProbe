package mcjty.theoneprobe.apiimpl;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.apiimpl.elements.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

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

        }
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(elements.size());
        for (Element element : elements) {

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
