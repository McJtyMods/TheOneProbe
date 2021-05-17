package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.*;

public class StyleManager implements IStyleManager {

    @Override
    public IEntityStyle entityStyleDefault() {
        return new EntityStyle();
    }

    @Override
    public IEntityStyle entityStyleBounded(int width, int height) {
        return new EntityStyle().bounds(width, height);
    }

    @Override
    public IEntityStyle entityStyleScaled(float scale) {
        return new EntityStyle().scale(scale);
    }

    @Override
    public IIconStyle iconStyleDefault() { return new IconStyle(); }

    @Override
    public IIconStyle iconStyleColored(Color color) { return new IconStyle().color(color); }

    @Override
    public IIconStyle iconStyleColored(int color) { return new IconStyle().color(color); }

    @Override
    public IIconStyle iconStyleBounded(int width, int height) { return new IconStyle().width(width).height(height); }

    @Override
    public IIconStyle iconStyleTextureBounded(int texWidth, int texHeight) { return new IconStyle().textureWidth(texWidth).textureHeight(texHeight); }

    @Override
    public IItemStyle itemStyleDefault() { return new ItemStyle();}

    @Override
    public IItemStyle itemStyleBounded(int width, int height) { return new ItemStyle().bounds(width, height);}

    @Override
    public ILayoutStyle layoutStyleDefault() { return new LayoutStyle(); }

    @Override
    public ILayoutStyle layoutStyleBordered(Color color) { return new LayoutStyle().borderColor(color); }

    @Override
    public ILayoutStyle layoutStyleBordered(Integer color) { return new LayoutStyle().borderColor(color); }

    @Override
    public ILayoutStyle layoutStyleSpaced(int spacing) { return new LayoutStyle().spacing(spacing); }

    @Override
    public ILayoutStyle layoutStyleAligned(ElementAlignment align) { return new LayoutStyle().alignment(align); }

    @Override
    public ILayoutStyle layoutStylePadded(int padding) { return new LayoutStyle().padding(padding); }

    @Override
    public ILayoutStyle layoutStylePadded(int xPadding, int yPadding) { return new LayoutStyle().hPadding(xPadding).vPadding(yPadding); }

    @Override
    public ILayoutStyle layoutStylePadded(int top, int bottom, int left, int right) { return new LayoutStyle().topPadding(top).bottomPadding(bottom).leftPadding(left).rightPadding(right); }

    @Override
    public IProgressStyle progressStyleDefault() { return new ProgressStyle(); }

    @Override
    public IProgressStyle progressStyleArmor() { return new ProgressStyle().armorBar(true); }

    @Override
    public IProgressStyle progressStyleLife() { return new ProgressStyle().lifeBar(true); }

    @Override
    public IProgressStyle progressStyleAligned(ElementAlignment align) { return new ProgressStyle().alignment(align); }

    @Override
    public IProgressStyle progressStyleBounded(int width, int height) { return new ProgressStyle().bounds(width, height); }

    @Override
    public IProgressStyle progressStyleTextOnly(String prefix) { return new ProgressStyle().prefix(prefix).numberFormat(NumberFormat.NONE); }

    @Override
    public IProgressStyle progressStyleText(String prefix, String suffix) { return new ProgressStyle().prefix(prefix).suffix(suffix); }

    @Override
    public ITextStyle textStyleDefault() { return new TextStyle(); }

    @Override
    public ITextStyle textStyleAligned(ElementAlignment align) { return new TextStyle().alignment(align); }

    @Override
    public ITextStyle textStyleWidth(Integer width) { return new TextStyle().width(width); }

    @Override
    public ITextStyle textStyleHeight(Integer height) { return new TextStyle().height(height); }

    @Override
    public ITextStyle textStyleBounded(Integer width, Integer height) { return new TextStyle().width(width).height(height); }

    @Override
    public ITextStyle textStylePadded(int padding) { return new TextStyle().padding(padding); }

    @Override
    public ITextStyle textStylePadded(int xPadding, int yPadding) { return new TextStyle().hPadding(yPadding).vPadding(xPadding); }

    @Override
    public ITextStyle textStylePadded(int top, int bottom, int left, int right) { return new TextStyle().topPadding(top).bottomPadding(bottom).leftPadding(left).rightPadding(right); }
}
