package mcjty.theoneprobe.api;

import mcjty.theoneprobe.api.Color;

import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;

/**
 * Style for a horizontal or vertical layout.
 */
public interface ILayoutStyle {
	
	static ILayoutStyle createDefault() { return new LayoutStyle(); }
	static ILayoutStyle createBorder(Color color) { return new LayoutStyle().borderColor(color); }
	static ILayoutStyle createBorder(Integer color) { return new LayoutStyle().borderColor(color); }
	static ILayoutStyle createSpacing(int spacing) { return new LayoutStyle().spacing(spacing); }
	static ILayoutStyle createAligned(ElementAlignment align) { return new LayoutStyle().alignment(align); }
	static ILayoutStyle createPadding(int padding) { return new LayoutStyle().padding(padding); }
	static ILayoutStyle createPadding(int xPadding, int yPadding) { return new LayoutStyle().hPadding(xPadding).vPadding(yPadding); }
	static ILayoutStyle createPadding(int top, int bottom, int left, int right) { return new LayoutStyle().topPadding(top).bottomPadding(bottom).leftPadding(left).rightPadding(right); }
	
	ILayoutStyle copy();
	
	default ILayoutStyle padding(int padding) { return topPadding(padding).bottomPadding(padding).leftPadding(padding).rightPadding(padding); }
	default ILayoutStyle vPadding(int padding) { return topPadding(padding).bottomPadding(padding); }
	default ILayoutStyle hPadding(int padding) { return leftPadding(padding).rightPadding(padding); }
	
	/// Padding Methods that allow you to add padding within the border color
	ILayoutStyle topPadding(int padding);
	ILayoutStyle bottomPadding(int padding);
	ILayoutStyle leftPadding(int padding);
	ILayoutStyle rightPadding(int padding);
	
    /// The color that is used for the border of the progress bar
    ILayoutStyle borderColor(Integer c);
    default ILayoutStyle borderColor(Color c) { return borderColor(c == null ? null : Integer.valueOf(c.getRGB())); }
    
    /**
     * The spacing to use between elements in this panel. -1 means to use default depending
     * on vertical vs horizontal.
     */
    ILayoutStyle spacing(int f);

    /**
     * Set the alignment of the elements inside this element. Default is ALIGN_TOPLEFT
     */
    ILayoutStyle alignment(ElementAlignment alignment);
    
	int getLeftPadding();
	int getRightPadding();
	int getTopPadding();
	int getBottomPadding();
    
    Integer getBorderColor();

    int getSpacing();

    ElementAlignment getAlignment();
}