package mcjty.theoneprobe.gui;

class Preset {
    private final String name;
    private final int boxBorderColor;
    private final int boxFillColor;
    private final int boxThickness;
    private final int boxOffset;

    public Preset(String name, int boxBorderColor, int boxFillColor, int boxThickness, int boxOffset) {
        this.name = name;
        this.boxBorderColor = boxBorderColor;
        this.boxFillColor = boxFillColor;
        this.boxThickness = boxThickness;
        this.boxOffset = boxOffset;
    }

    public String getName() {
        return name;
    }

    public int getBoxBorderColor() {
        return boxBorderColor;
    }

    public int getBoxFillColor() {
        return boxFillColor;
    }

    public int getBoxThickness() {
        return boxThickness;
    }

    public int getBoxOffset() {
        return boxOffset;
    }
}
