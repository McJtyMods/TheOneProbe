package mcjty.theoneprobe.api;

public class Color {
    private final int value;

    public Color(int value) {
        this.value = value;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int r, int g, int b, int a) {
        this.value = (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | (b & 255) << 0;
    }

    public Color darker() {
        return new Color(Math.max((int)((double)this.getRed() * 0.7D), 0), Math.max((int)((double)this.getGreen() * 0.7D), 0), Math.max((int)((double)this.getBlue() * 0.7D), 0), this.getAlpha());
    }

    public int getRed() {
        return this.getRGB() >> 16 & 255;
    }

    public int getGreen() {
        return this.getRGB() >> 8 & 255;
    }

    public int getBlue() {
        return this.getRGB() >> 0 & 255;
    }

    public int getAlpha() {
        return this.getRGB() >> 24 & 255;
    }

    public int getRGB() {
        return this.value;
    }

}
