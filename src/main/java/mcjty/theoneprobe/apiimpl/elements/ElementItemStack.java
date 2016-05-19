package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ElementItemStack implements IElement {

    private final ItemStack itemStack;

    public ElementItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ElementItemStack(ByteBuf buf) {
        if (buf.readBoolean()) {
            itemStack = NetworkTools.readItemStack(buf);
        } else {
            itemStack = null;
        }
    }

    @Override
    public void render(int x, int y) {
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        if (itemStack != null) {
            String amount = itemStack.stackSize > 1 ? Integer.toString(itemStack.stackSize) : "";
            RenderHelper.renderItemStack(Minecraft.getMinecraft(), itemRender, itemStack, x, y, amount);
        }
    }

    @Override
    public int getWidth() {
        return 20;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (itemStack != null) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, itemStack);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ITEM;
    }
}
