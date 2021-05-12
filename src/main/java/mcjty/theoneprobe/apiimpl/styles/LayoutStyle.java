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
    
    private int top = 0;
    private int bottom = 0;
    private int left = 0;
    private int right = 0;
    
    @Override
    public ILayoutStyle copy() {
    	return new LayoutStyle().borderColor(borderColor).alignment(alignment).spacing(spacing).bottomPadding(bottom).leftPadding(left).topPadding(top).rightPadding(right);
    }
    
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
    
	@Override
	public LayoutStyle topPadding(int padding) {
		top = padding;
		return this;
	}
	
	@Override
	public LayoutStyle bottomPadding(int padding) {
		bottom = padding;
		return this;
	}
	
	@Override
	public LayoutStyle leftPadding(int padding) {
		left = padding;
		return this;
	}
	
	@Override
	public LayoutStyle rightPadding(int padding) {
		right = padding;
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
    
	@Override
	public int getLeftPadding() {
		return left;
	}
	
	@Override
	public int getRightPadding() {
		return right;
	}
	
	@Override
	public int getTopPadding() {
		return top;
	}
	
	@Override
	public int getBottomPadding() {
		return bottom;
	}
}