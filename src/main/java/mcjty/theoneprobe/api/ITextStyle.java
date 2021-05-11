package mcjty.theoneprobe.api;

import mcjty.theoneprobe.apiimpl.styles.TextStyle;

/**
 * Style for the text element.
 */
public interface ITextStyle {
	
	public static ITextStyle createDefault() { return new TextStyle(); }
	public static ITextStyle createAligned(ElementAlignment align) { return new TextStyle().alignment(align); }
	public static ITextStyle createWidth(Integer width) { return new TextStyle().width(width); }
	public static ITextStyle createHeight(Integer height) { return new TextStyle().height(height); }
	public static ITextStyle createBounds(Integer width, Integer height) { return new TextStyle().width(width).height(height); }
	public static ITextStyle createPadding(int padding) { return new TextStyle().padding(padding); }
	public static ITextStyle createPadding(int xPadding, int yPadding) { return new TextStyle().hPadding(yPadding).vPadding(xPadding); }
	public static ITextStyle createPadding(int top, int bottom, int left, int right) { return new TextStyle().topPadding(top).bottomPadding(bottom).leftPadding(left).rightPadding(right); }
	
	public default ITextStyle padding(int padding) { return topPadding(padding).bottomPadding(padding).leftPadding(padding).rightPadding(padding); }
	public default ITextStyle vPadding(int padding) { return topPadding(padding).bottomPadding(padding); }
	public default ITextStyle hPadding(int padding) { return leftPadding(padding).rightPadding(padding); }

	public ITextStyle topPadding(int padding);
	public ITextStyle bottomPadding(int padding);
	public ITextStyle leftPadding(int padding);
	public ITextStyle rightPadding(int padding);
	public default ITextStyle bounds(Integer width, Integer height) { return width(width).height(height); }
	public ITextStyle width(Integer width);
	public ITextStyle height(Integer height);
	public ITextStyle alignment(ElementAlignment align);
	
	public int getLeftPadding();
	public int getRightPadding();
	public int getTopPadding();
	public int getBottomPadding();
	public Integer getWidth();
	public Integer getHeight();
	public ElementAlignment getAlignment();
}
