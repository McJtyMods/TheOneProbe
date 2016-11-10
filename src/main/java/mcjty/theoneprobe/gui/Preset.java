package mcjty.theoneprobe.gui;

class Preset {
    private final String name;
    private final int boxBorderColor;
    private final int boxFillColor;
    private final int boxThickness;

    public Preset(String name, int boxBorderColor, int boxFillColor, int boxThickness) {
        this.name = name;
        this.boxBorderColor = boxBorderColor;
        this.boxFillColor = boxFillColor;
        this.boxThickness = boxThickness;
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
}
