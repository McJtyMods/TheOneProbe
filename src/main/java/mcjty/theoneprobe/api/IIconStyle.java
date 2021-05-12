package mcjty.theoneprobe.api;

import java.awt.Color;

import mcjty.theoneprobe.apiimpl.styles.IconStyle;

/**
 * Style for the icon element.
 */
public interface IIconStyle {
	public static IIconStyle createDefault() { return new IconStyle(); }
	public static IIconStyle createColor(Color color) { return new IconStyle().color(color); }
	public static IIconStyle createColor(int color) { return new IconStyle().color(color); }
	public static IIconStyle createBounds(int width, int height) { return new IconStyle().width(width).height(height); }
	public static IIconStyle createTexBounds(int texWidth, int texHeight) { return new IconStyle().textureWidth(texWidth).textureHeight(texHeight); }
	
	
	IIconStyle copy();
    /**
     * Change the width of the icon. Default is 16
     */
    IIconStyle width(int w);

    /**
     * Change the height of the icon. Default is 16
     */
    IIconStyle height(int h);
    default IIconStyle bounds(int w, int h) { return width(w).height(h); }

    int getWidth();
    int getHeight();

    /**
     * Change the total width of the texture on which the icon sits. Default is 256
     */
    IIconStyle textureWidth(int w);

    /**
     * Change the total height of the texture on which the icon sits. Default is 256
     */
    IIconStyle textureHeight(int h);
    default IIconStyle textureBounds(int w, int h) { return textureWidth(w).textureHeight(h); }

    int getTextureWidth();
    int getTextureHeight();
    
    IIconStyle color(int color);
    default IIconStyle color(Color color) { return color(color.getRGB());}
    
    int getColor();
}
