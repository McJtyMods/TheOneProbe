package mcjty.theoneprobe.api;

/**
 * Style for a horizonatl or vertical layout.
 */
public class LayoutStyle {
    private Integer borderColor = null;
    private int spacing = -1;

    /// The color that is used for the border of the progress bar
    public LayoutStyle borderColor(Integer c) {
        borderColor = c;
        return this;
    }

    /**
     * The spacing to use between elements in this panel. -1 means to use default depending
     * on vertical vs horizontal.
     */
    public LayoutStyle spacing(int f) {
        spacing = f;
        return this;
    }

    public Integer getBorderColor() {
        return borderColor;
    }

    public int getSpacing() {
        return spacing;
    }
}
