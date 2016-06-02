package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ElementItemStackRender {

    public static void render(ItemStack itemStack, IItemStyle style, int x, int y) {
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        if (itemStack != null) {
            String amount = itemStack.stackSize > 1 ? Integer.toString(itemStack.stackSize) : "";
            RenderHelper.renderItemStack(Minecraft.getMinecraft(), itemRender, itemStack, x + (style.getWidth() - 18) / 2, y + (style.getHeight() - 18) / 2, amount);
        }
    }

}
