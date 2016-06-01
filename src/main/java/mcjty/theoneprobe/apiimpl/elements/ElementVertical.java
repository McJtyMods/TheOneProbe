package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;

public class ElementVertical extends AbstractElementPanel {

    public static final int SPACING = 2;

    public ElementVertical(Integer borderColor, int spacing, ElementAlignment alignment) {
        super(borderColor, spacing, alignment);
    }

    public ElementVertical(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void render(int x, int y) {
        super.render(x, y);
        if (borderColor != null) {
            x += 3;
            y += 3;
        }
        int totWidth = getWidth();
        for (IElement element : children) {
            int w = element.getWidth();
            int cx = x;
            switch (alignment) {
                case ALIGN_TOPLEFT:
                    break;
                case ALIGN_CENTER:
                    cx = x + (totWidth - w) / 2;
                    break;
                case ALIGN_BOTTOMRIGHT:
                    cx = x + totWidth - w;
                    break;
            }
            element.render(cx, y);
            y += element.getHeight() + spacing;
        }
    }

    private int getBorderSpacing() {
        return borderColor == null ? 0 : 6;
    }

    @Override
    public int getHeight() {
        int h = 0;
        for (IElement element : children) {
            h += element.getHeight();
        }
        return h + spacing * (children.size() - 1) + getBorderSpacing();
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (IElement element : children) {
            int ww = element.getWidth();
            if (ww > w) {
                w = ww;
            }
        }
        return w + getBorderSpacing();
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_VERTICAL;
    }
}
