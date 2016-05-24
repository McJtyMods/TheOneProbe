package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IIconStyle;

public class IconStyle implements IIconStyle {
    private int width = 16;
    private int height = 16;

    @Override
    public IIconStyle width(int w) {
        width = w;
        return this;
    }

    @Override
    public IIconStyle height(int h) {
        height = h;
        return this;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
