package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ElementVertical extends AbstractElementPanel {

    public static final int SPACING = 2;
    
    public ElementVertical(){
    	super(new LayoutStyle());
    }
    
    public ElementVertical(ILayoutStyle style) {
    	super(style);
    }
    
    @Deprecated
    public ElementVertical(Integer borderColor, int spacing, ElementAlignment alignment) {
        super(borderColor, spacing, alignment);
    }

    public ElementVertical(RegistryFriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        super.render(graphics, x, y);
        if (layout.getBorderColor() != null) {
            x += 3;
            y += 3;
        }
		y += layout.getTopPadding();
		int totWidth = getWidth() - getXPadding();
        for (IElement element : children) {
            int w = element.getWidth();
            int cx = x;
            switch (layout.getAlignment()) {
                case ALIGN_TOPLEFT:
                    break;
                case ALIGN_CENTER:
                    cx = x + (totWidth - w) / 2;
                    break;
                case ALIGN_BOTTOMRIGHT:
                    cx = x + totWidth - w;
                    break;
            }
            element.render(graphics, cx + layout.getLeftPadding(), y);
            y += element.getHeight() + layout.getSpacing();
        }
    }
    
    private int getBorderSpacing() {
        return layout.getBorderColor() == null ? 0 : 6;
    }

    @Override
    public int getHeight() {
        int h = 0;
        for (IElement element : children) {
            h += element.getHeight();
        }
        return h + (layout.getSpacing() * (children.size() - 1)) + getBorderSpacing() + getYPadding();
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (IElement element : children) {
        	w = Math.max(w, element.getWidth());
        }
        return w + getBorderSpacing() + getXPadding();
    }

    @Override
    public ResourceLocation getID() {
        return TheOneProbeImp.ELEMENT_VERTICAL;
    }
}
