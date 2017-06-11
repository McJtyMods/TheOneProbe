package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.item.ItemStack;

public class ElementItemLabel implements IElement {

    private final ItemStack itemStack;

    public ElementItemLabel(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ElementItemLabel(ByteBuf buf) {
        if (buf.readBoolean()) {
            itemStack = NetworkTools.readItemStack(buf);
        } else {
            itemStack = ItemStack.EMPTY;
        }
    }

    @Override
    public void render(int x, int y) {
        if (!itemStack.isEmpty()) {
            String text = itemStack.getDisplayName();
            ElementTextRender.render(text, x, y);
        }
    }

    @Override
    public int getWidth() {
        if (!itemStack.isEmpty()) {
            String text = itemStack.getDisplayName();
            return ElementTextRender.getWidth(text);
        } else {
            return 10;
        }
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!itemStack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, itemStack);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ITEMLABEL;
    }
}
