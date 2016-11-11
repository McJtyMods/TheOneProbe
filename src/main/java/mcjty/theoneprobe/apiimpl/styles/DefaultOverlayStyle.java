package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.IOverlayStyle;

public class DefaultOverlayStyle implements IOverlayStyle {

    private int borderThickness;
    private int borderColor;
    private int boxColor;
    private int borderOffset;
    private int leftX;
    private int rightX;
    private int topY;
    private int bottomY;

    public IOverlayStyle copy() {
        return new DefaultOverlayStyle()
                .borderThickness(borderThickness)
                .borderColor(borderColor)
                .boxColor(boxColor)
                .borderOffset(borderOffset)
                .location(leftX, rightX, topY, bottomY);
    }

    @Override
    public IOverlayStyle borderOffset(int offset) {
        borderOffset = offset;
        return this;
    }

    @Override
    public int getBorderOffset() {
        return borderOffset;
    }

    @Override
    public IOverlayStyle borderThickness(int thick) {
        borderThickness = thick;
        return this;
    }

    @Override
    public int getBorderThickness() {
        return borderThickness;
    }

    @Override
    public IOverlayStyle borderColor(int color) {
        borderColor = color;
        return this;
    }

    @Override
    public int getBorderColor() {
        return borderColor;
    }

    @Override
    public IOverlayStyle boxColor(int color) {
        boxColor = color;
        return this;
    }

    @Override
    public int getBoxColor() {
        return boxColor;
    }

    @Override
    public IOverlayStyle location(int leftX, int rightX, int topY, int bottomY) {
        this.leftX = leftX;
        this.rightX = rightX;
        this.topY = topY;
        this.bottomY = bottomY;
        return this;
    }

    @Override
    public int getLeftX() {
        return leftX;
    }

    @Override
    public int getRightX() {
        return rightX;
    }

    @Override
    public int getTopY() {
        return topY;
    }

    @Override
    public int getBottomY() {
        return bottomY;
    }
}
