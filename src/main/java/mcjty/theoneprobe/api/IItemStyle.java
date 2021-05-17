package mcjty.theoneprobe.api;

/**
 * Style for the item element.
 */
public interface IItemStyle {

	IItemStyle copy();
	
	IItemStyle width(int w);
    IItemStyle height(int h);
    default IItemStyle bounds(int width, int height) { return width(width).height(height); }
    
    int getWidth();
    int getHeight();
}
