package mcjty.theoneprobe.api;

import mcjty.theoneprobe.apiimpl.styles.ItemStyle;

/**
 * Style for the item element.
 */
public interface IItemStyle {
	static IItemStyle createDefault() { return new ItemStyle();}
	static IItemStyle createBounds(int width, int height) { return new ItemStyle().bounds(width, height);}
	
	IItemStyle copy();
	
	IItemStyle width(int w);
    IItemStyle height(int h);
    default IItemStyle bounds(int width, int height) { return width(width).height(height); }
    
    int getWidth();
    int getHeight();
}
