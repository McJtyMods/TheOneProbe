package mcjty.theoneprobe.api;

import mcjty.theoneprobe.api.Color;

import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Style for the progress bar.
 */
public interface IProgressStyle {
	
	/// Default Creation Methods that allow to allow create default instances for most basic cases
	static IProgressStyle createDefault() { return new ProgressStyle(); }
	static IProgressStyle createArmor() { return new ProgressStyle().armorBar(true); }
	static IProgressStyle createLife() { return new ProgressStyle().lifeBar(true); }
	static IProgressStyle createAligned(ElementAlignment align) { return new ProgressStyle().alignment(align); }
	static IProgressStyle createBounds(int width, int height) { return new ProgressStyle().bounds(width, height); }
	static IProgressStyle createTextOnly(String prefix) { return new ProgressStyle().prefix(prefix).numberFormat(NumberFormat.NONE); }
	static IProgressStyle createText(String prefix, String suffix) { return new ProgressStyle().prefix(prefix).suffix(suffix); }
	
	/// Allows copying the state for easier template creation
	IProgressStyle copy();
    /// The color that is used for the border of the progress bar
    IProgressStyle borderColor(int c);
    default IProgressStyle borderColor(Color c) { return borderColor(c.getRGB()); }

    /// The color that is used for the background of the progress bar
    IProgressStyle backgroundColor(int c);
    default IProgressStyle backgroundColor(Color c) { return backgroundColor(c.getRGB()); }

    /// The color that is used for the filled part of the progress bar
    IProgressStyle filledColor(int c);
    default IProgressStyle filledColor(Color c) { return filledColor(c.getRGB()); }

    /// If this is different from the filledColor then the fill color will alternate
    IProgressStyle alternateFilledColor(int c);
    default IProgressStyle alternateFilledColor(Color c) { return alternateFilledColor(c.getRGB()); }
    
    /// Helper functions to compress code
    default IProgressStyle borderlessColor(int filled, int background) { return filledColor(filled).backgroundColor(background); }
    default IProgressStyle borderlessColor(int filled, int alternate, int background) { return filledColor(filled).alternateFilledColor(alternate).backgroundColor(background); }
    default IProgressStyle borderlessColor(Color filled, Color background) { return filledColor(filled).backgroundColor(background); }
    default IProgressStyle borderlessColor(Color filled, Color alternate, Color background) { return filledColor(filled).alternateFilledColor(alternate).backgroundColor(background); }
    default IProgressStyle color(int border, int filled, int background) { return borderColor(border).filledColor(filled).backgroundColor(background); }
    default IProgressStyle color(int border, int filled, int alternate, int background) { return borderColor(border).filledColor(filled).alternateFilledColor(alternate).backgroundColor(background); }
    default IProgressStyle color(Color border, Color filled, Color background) { return borderColor(border).filledColor(filled).backgroundColor(background); }
    default IProgressStyle color(Color border, Color filled, Color alternate, Color background) { return borderColor(border).filledColor(filled).alternateFilledColor(alternate).backgroundColor(background); }
    
    /// If true then text is shown inside the progress bar
    IProgressStyle showText(boolean b);

    /// The number format to use for the text inside the progress bar
    IProgressStyle numberFormat(NumberFormat f);
    
    IProgressStyle prefix(ITextComponent prefix);
    IProgressStyle suffix(ITextComponent suffix);
    
    default IProgressStyle prefix(String prefix, Object...args) { return prefix(new TranslationTextComponent(prefix, args)); }
    default IProgressStyle suffix(String suffix, Object...args){ return suffix(new TranslationTextComponent(suffix, args)); }
    default IProgressStyle prefix(String prefix) { return prefix(new TranslationTextComponent(prefix)); }
    default IProgressStyle suffix(String suffix){ return suffix(new TranslationTextComponent(suffix)); }
    
    /// If the progressbar is a lifebar then this is the maximum width
    default IProgressStyle bounds(int width, int height) { return width(width).height(height); }
    IProgressStyle width(int w);
    IProgressStyle height(int h);
    IProgressStyle alignment(ElementAlignment align);
    
    IProgressStyle lifeBar(boolean b);
    IProgressStyle armorBar(boolean b);

    int getBorderColor();
    int getBackgroundColor();
    int getFilledColor();
    int getAlternatefilledColor();
    
    boolean isShowText();
    NumberFormat getNumberFormat();
    
    String getPrefix();
    String getSuffix();
    ITextComponent getPrefixComp();
    ITextComponent getSuffixComp();
    
    int getWidth();
    int getHeight();
    ElementAlignment getAlignment();
    
    boolean isLifeBar();
    boolean isArmorBar();
}
