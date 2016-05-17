package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ElementItemStack implements Element {

    private final ItemStack itemStack;

    public ElementItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ElementItemStack(ByteBuf buf) {
        itemStack = NetworkTools.readItemStack(buf);
    }

    @Override
    public void render(Cursor cursor) {
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        RenderHelper.renderItemStack(Minecraft.getMinecraft(), itemRender, itemStack, cursor.getX(), cursor.getY(), "");
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
        NetworkTools.writeItemStack(buf, itemStack);
    }

    @Override
    public ElementType getType() {
        return ElementType.ITEMSTACK;
    }
}
