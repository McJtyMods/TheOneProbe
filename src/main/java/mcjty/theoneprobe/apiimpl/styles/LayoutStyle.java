package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ILayoutStyle;

/**
 * Style for a horizonatl or vertical layout.
 */
public class LayoutStyle implements ILayoutStyle {
    private Integer borderColor = null;
    private ElementAlignment alignment = ElementAlignment.ALIGN_TOPLEFT;
    private int spacing = -1;

    @Override
    public ILayoutStyle alignment(ElementAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @Override
    public ElementAlignment getAlignment() {
        return alignment;
    }

    /// The color that is used for the border of the progress bar
    @Override
    public LayoutStyle borderColor(Integer c) {
        borderColor = c;
        return this;
    }

    /**
     * The spacing to use between elements in this panel. -1 means to use default depending
     * on vertical vs horizontal.
     */
    @Override
    public LayoutStyle spacing(int f) {
        spacing = f;
        return this;
    }

    @Override
    public Integer getBorderColor() {
        return borderColor;
    }

    @Override
    public int getSpacing() {
        return spacing;
    }
}
