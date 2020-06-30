package mcjty.theoneprobe.apiimpl.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ElementItemLabel implements IElement {

    private final ItemStack itemStack;

    public ElementItemLabel(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ElementItemLabel(PacketBuffer buf) {
        if (buf.readBoolean()) {
            itemStack = NetworkTools.readItemStack(buf);
        } else {
            itemStack = ItemStack.EMPTY;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y) {
        if (!itemStack.isEmpty()) {
            String text = itemStack.getDisplayName().getString();
            ElementTextRender.render(text, matrixStack, x, y);
        }
    }

    @Override
    public int getWidth() {
        if (!itemStack.isEmpty()) {
            String text = itemStack.getDisplayName().getString();
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
    public void toBytes(PacketBuffer buf) {
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
