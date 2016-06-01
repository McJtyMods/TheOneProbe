package mcjty.theoneprobe.api;

/**
 * Style for the icon element.
 */
public interface IIconStyle {
    /**
     * Change the width of the icon. Default is 16
     */
    IIconStyle width(int w);

    /**
     * Change the height of the icon. Default is 16
     */
    IIconStyle height(int h);

    int getWidth();

    int getHeight();
}
