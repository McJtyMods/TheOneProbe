package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ElementItemLabel implements IElement {

    private final ItemStack itemStack;

    public ElementItemLabel(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ElementItemLabel(RegistryFriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            itemStack = NetworkTools.readItemStack(buf);
        } else {
            itemStack = ItemStack.EMPTY;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        if (!itemStack.isEmpty()) {
            ElementTextRender.render(itemStack.getHoverName(), graphics, x, y);
        }
    }

    @Override
    public int getWidth() {
        if (!itemStack.isEmpty()) {
            return ElementTextRender.getLegacyWidth(itemStack.getHoverName());
        } else {
            return 10;
        }
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        if (!itemStack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, itemStack);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public ResourceLocation getID() {
        return TheOneProbeImp.ELEMENT_ITEMLABEL;
    }
}
