package mcjty.theoneprobe.api;

import mcjty.theoneprobe.apiimpl.styles.EntityStyle;

/**
 * Style for the entity element.
 */
public interface IEntityStyle {
	static IEntityStyle createDefault() { return new EntityStyle(); }
	static IEntityStyle createBounds(int width, int height) { return new EntityStyle().bounds(width, height); }
	static IEntityStyle createScale(float scale) { return new EntityStyle().scale(scale); }
	
	IEntityStyle copy();
	
    /**
     * Change the width of the element. Default is 25
     */
    IEntityStyle width(int w);

    /**
     * Change the height of the element. Default is 25
     */
    IEntityStyle height(int h);
    default IEntityStyle bounds(int width, int height) { return width(width).height(height); }

    /**
     * Change the scale of the entity inside the element. Default is 1.0 which
     * tries to fit as good as possible.
     */
    IEntityStyle scale(float scale);

    int getWidth();

    int getHeight();

    float getScale();
}
