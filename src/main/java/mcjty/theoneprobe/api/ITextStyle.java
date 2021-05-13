package mcjty.theoneprobe.api;

import mcjty.theoneprobe.apiimpl.styles.TextStyle;

/**
 * Style for the text element.
 */
public interface ITextStyle {
	
	static ITextStyle createDefault() { return new TextStyle(); }
	static ITextStyle createAligned(ElementAlignment align) { return new TextStyle().alignment(align); }
	static ITextStyle createWidth(Integer width) { return new TextStyle().width(width); }
	static ITextStyle createHeight(Integer height) { return new TextStyle().height(height); }
	static ITextStyle createBounds(Integer width, Integer height) { return new TextStyle().width(width).height(height); }
	static ITextStyle createPadding(int padding) { return new TextStyle().padding(padding); }
	static ITextStyle createPadding(int xPadding, int yPadding) { return new TextStyle().hPadding(yPadding).vPadding(xPadding); }
	static ITextStyle createPadding(int top, int bottom, int left, int right) { return new TextStyle().topPadding(top).bottomPadding(bottom).leftPadding(left).rightPadding(right); }
	
	/// Allows copying the state for easier template creation
	ITextStyle copy();
	
	default ITextStyle padding(int padding) { return topPadding(padding).bottomPadding(padding).leftPadding(padding).rightPadding(padding); }
	default ITextStyle vPadding(int padding) { return topPadding(padding).bottomPadding(padding); }
	default ITextStyle hPadding(int padding) { return leftPadding(padding).rightPadding(padding); }

	ITextStyle topPadding(int padding);
	ITextStyle bottomPadding(int padding);
	ITextStyle leftPadding(int padding);
	ITextStyle rightPadding(int padding);
	default ITextStyle bounds(Integer width, Integer height) { return width(width).height(height); }
	ITextStyle width(Integer width);
	ITextStyle height(Integer height);
	ITextStyle alignment(ElementAlignment align);
	
	int getLeftPadding();
	int getRightPadding();
	int getTopPadding();
	int getBottomPadding();
	Integer getWidth();
	Integer getHeight();
	ElementAlignment getAlignment();
}
