package mcjty.theoneprobe.api;

/**
 * Style for a horizonatl or vertical layout.
 */
public interface ILayoutStyle {
    /// The color that is used for the border of the progress bar
    ILayoutStyle borderColor(Integer c);

    /**
     * The spacing to use between elements in this panel. -1 means to use default depending
     * on vertical vs horizontal.
     */
    ILayoutStyle spacing(int f);

    Integer getBorderColor();

    int getSpacing();
}
