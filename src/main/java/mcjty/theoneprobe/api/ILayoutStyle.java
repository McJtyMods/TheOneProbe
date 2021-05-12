package mcjty.theoneprobe.api;

import java.awt.Color;

import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;

/**
 * Style for a horizonatl or vertical layout.
 */
public interface ILayoutStyle {
	
	public static ILayoutStyle createDefault() { return new LayoutStyle(); }
	public static ILayoutStyle createBorder(Color color) { return new LayoutStyle().borderColor(color); }
	public static ILayoutStyle createBorder(Integer color) { return new LayoutStyle().borderColor(color); }
	public static ILayoutStyle createSpacing(int spacing) { return new LayoutStyle().spacing(spacing); }
	public static ILayoutStyle createAligned(ElementAlignment align) { return new LayoutStyle().alignment(align); }
	public static ILayoutStyle createPadding(int padding) { return new LayoutStyle().padding(padding); }
	public static ILayoutStyle createPadding(int xPadding, int yPadding) { return new LayoutStyle().hPadding(xPadding).vPadding(yPadding); }
	public static ILayoutStyle createPadding(int top, int bottom, int left, int right) { return new LayoutStyle().topPadding(top).bottomPadding(bottom).leftPadding(left).rightPadding(right); }
	
	public ILayoutStyle copy();
	
	public default ILayoutStyle padding(int padding) { return topPadding(padding).bottomPadding(padding).leftPadding(padding).rightPadding(padding); }
	public default ILayoutStyle vPadding(int padding) { return topPadding(padding).bottomPadding(padding); }
	public default ILayoutStyle hPadding(int padding) { return leftPadding(padding).rightPadding(padding); }
	
	/// Padding Methods that allow you to add padding within the border color
	public ILayoutStyle topPadding(int padding);
	public ILayoutStyle bottomPadding(int padding);
	public ILayoutStyle leftPadding(int padding);
	public ILayoutStyle rightPadding(int padding);
	
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