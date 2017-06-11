package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementItemStackRender;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.item.ItemStack;

public class ElementItemStack implements IElement {

    private final ItemStack itemStack;
    private final IItemStyle style;

    public ElementItemStack(ItemStack itemStack, IItemStyle style) {
        this.itemStack = itemStack;
        this.style = style;
    }

    public ElementItemStack(ByteBuf buf) {
        if (buf.readBoolean()) {
            itemStack = NetworkTools.readItemStack(buf);
        } else {
            itemStack = ItemStack.EMPTY;
        }
        style = new ItemStyle()
                .width(buf.readInt())
                .height(buf.readInt());
    }

    @Override
    public void render(int x, int y) {
        ElementItemStackRender.render(itemStack, style, x, y);
    }

    @Override
    public int getWidth() {
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return style.getHeight();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!itemStack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, itemStack);
        } else {
            buf.writeBoolean(false);
        }
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ITEM;
    }
}
