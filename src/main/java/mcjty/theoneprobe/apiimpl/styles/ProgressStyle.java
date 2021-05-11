package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Style for the progress bar.
 */
public class ProgressStyle implements IProgressStyle {
    private int borderColor = 0xffffffff;
    private int backgroundColor = 0xff000000;
    private int filledColor = 0xffaaaaaa;
    private int alternatefilledColor = 0xffaaaaaa;
    private boolean showText = true;
    private int width = 100;
    private int height = 12;
    private boolean lifeBar = false;
    private boolean armorBar = false;
	private ElementAlignment alignment = ElementAlignment.ALIGN_TOPLEFT;
	private ITextComponent prefix = StringTextComponent.EMPTY;
	private ITextComponent suffix = StringTextComponent.EMPTY;

    private NumberFormat numberFormat = NumberFormat.FULL;
    
    @Override
    public IProgressStyle copy()
    {
    	return new ProgressStyle()
    			.borderColor(borderColor).backgroundColor(backgroundColor).filledColor(filledColor).alternateFilledColor(alternatefilledColor)
    			.showText(showText).width(width).height(height).lifeBar(lifeBar).armorBar(armorBar)
    			.alignment(alignment).prefix(prefix).suffix(suffix).numberFormat(numberFormat);
    }
    
    /// The color that is used for the border of the progress bar
    @Override
    public ProgressStyle borderColor(int c) {
        borderColor = c;
        return this;
    }

    /// The color that is used for the background of the progress bar
    @Override
    public ProgressStyle backgroundColor(int c) {
        backgroundColor = c;
        return this;
    }

    /// The color that is used for the filled part of the progress bar
    @Override
    public ProgressStyle filledColor(int c) {
        filledColor = c;
        return this;
    }

    /// If this is different from the filledColor then the fill color will alternate
    @Override
    public ProgressStyle alternateFilledColor(int c) {
        alternatefilledColor = c;
        return this;
    }

    /// If true then text is shown inside the progress bar
    @Override
    public ProgressStyle showText(boolean b) {
        showText = b;
        return this;
    }

    /// The number format to use for the text inside the progress bar
    @Override
    public ProgressStyle numberFormat(NumberFormat f) {
        numberFormat = f;
        return this;
    }

    @Override
    public ProgressStyle prefix(String prefix) {
        return prefix(new TranslationTextComponent(prefix));
    }

    @Override
    public ProgressStyle suffix(String suffix) {
        return suffix(new TranslationTextComponent(suffix));
    }
    
    @Override
	public ProgressStyle prefix(ITextComponent prefix) {
    	this.prefix = prefix;
		return this;
	}

	@Override
	public ProgressStyle suffix(ITextComponent suffix) {
    	this.suffix = suffix;
		return this;
	}

	@Override
	public ProgressStyle alignment(ElementAlignment align) {
		this.alignment = align;
		return this;
	}
	
	@Override
    public ProgressStyle width(int w) {
        this.width = w;
        return this;
    }

    @Override
    public ProgressStyle height(int h) {
        this.height = h;
        return this;
    }

    @Override
    public IProgressStyle lifeBar(boolean b) {
        this.lifeBar = b;
        return this;
    }

    @Override
    public IProgressStyle armorBar(boolean b) {
        this.armorBar = b;
        return this;
    }

    @Override
    public int getBorderColor() {
        return borderColor;
    }

    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public int getFilledColor() {
        return filledColor;
    }

    @Override
    public int getAlternatefilledColor() {
        return alternatefilledColor;
    }

    @Override
    public boolean isShowText() {
        return showText;
    }

    @Override
    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    @Override
    public String getPrefix() {
        return prefix.getString();
    }

    @Override
    public String getSuffix() {
        return suffix.getString();
    }
    
	@Override
	public ITextComponent getPrefixComp() {
		return prefix;
	}

	@Override
	public ITextComponent getSuffixComp() {
		return suffix;
	}

	@Override
	public ElementAlignment getAlignment() {
		return alignment;
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
    public boolean isLifeBar() {
        return lifeBar;
    }

    @Override
    public boolean isArmorBar() {
        return armorBar;
    }
}
