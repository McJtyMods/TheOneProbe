package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ElementItemStack implements Element {

    private final ItemStack itemStack;

    public ElementItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void render(Cursor cursor) {
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        int w = RenderHelper.renderItemStack(Minecraft.getMinecraft(), itemRender, itemStack, cursor.getX(), cursor.getY(), "");
        cursor.addX(w);
        cursor.updateMaxY(20);
    }
}
