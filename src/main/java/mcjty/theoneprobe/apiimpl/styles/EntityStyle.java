package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.IEntityStyle;

public class EntityStyle implements IEntityStyle {
    private int width = 25;
    private int height = 25;
    private float scale = 1.0f;

    @Override
    public IEntityStyle width(int w) {
        this.width = w;
        return this;
    }

    @Override
    public IEntityStyle height(int h) {
        this.height = h;
        return this;
    }

    @Override
    public IEntityStyle scale(float scale) {
        this.scale = scale;
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

    @Override
    public float getScale() {
        return scale;
    }
}
