package mcjty.theoneprobe.apiimpl.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import net.minecraft.network.PacketBuffer;

public class ElementHorizontal extends AbstractElementPanel {

    public static final int SPACING = 5;
    
    public ElementHorizontal(ILayoutStyle style) {
    	super(style);
	}
    
    @Deprecated
    public ElementHorizontal(Integer borderColor, int spacing, ElementAlignment alignment) {
        super(borderColor, spacing, alignment);
    }

    public ElementHorizontal(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y) {
        super.render(matrixStack, x, y);
        if (layout.getBorderColor() != null) {
            x += 3;
            y += 3;
        }
		x += layout.getLeftPadding();
		int totHeight = getHeight() - getYPadding();
        for (IElement element : children) {
            int h = element.getHeight();
            int cy = y;
            switch (layout.getAlignment()) {
                case ALIGN_TOPLEFT:
                    break;
                case ALIGN_CENTER:
                    cy = y + (totHeight - h) / 2;
                    break;
                case ALIGN_BOTTOMRIGHT:
                    cy = y + totHeight - h;
                    break;
            }
            element.render(matrixStack, x, cy + layout.getTopPadding());
            x += element.getWidth() + layout.getSpacing();
        }
    }

    private int getBorderSpacing() {
        return layout.getBorderColor() == null ? 0 : 6;
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (IElement element : children) {
            w += element.getWidth();
        }
        return w + (layout.getSpacing() * (children.size() - 1)) + getBorderSpacing() + getXPadding();
    }

    @Override
    public int getHeight() {
        int h = 0;
        for (IElement element : children) {
        	h = Math.max(h, element.getHeight());
        }
        return h + getBorderSpacing() + getYPadding();
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_HORIZONTAL;
    }
}
