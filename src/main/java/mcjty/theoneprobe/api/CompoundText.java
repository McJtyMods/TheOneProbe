package mcjty.theoneprobe.api;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CompoundText {

    private ITextComponent component = null;

    public static CompoundText create() {
        return new CompoundText();
    }

    /// Use this to set a style that the player can configure client-side. This is the recommended way to do text formatting
    public CompoundText style(TextStyleClass style) {
        ITextComponent cmp = new StringTextComponent(style.toString());
        return newComponent(cmp);
    }

    /// The prefered way to do translatable text
    public CompoundText text(ITextComponent cmp) {
        return newComponent(cmp);
    }

    /// Only use this for small strings or numbers for which no translation is useful
    public CompoundText text(String text) {
        return newComponent(new StringTextComponent(text));
    }

    /// Shorthand for style(TextStyleClass.INFO).text(new TranslationTextComponent(translationKey):
    public CompoundText info(String translationKey) {
        return style(TextStyleClass.INFO).text(new TranslationTextComponent(translationKey));
    }

    /// Shorthand for style(TextStyleClass.INFOIMP).text(new TranslationTextComponent(translationKey):
    public CompoundText important(String translationKey) {
        return style(TextStyleClass.INFOIMP).text(new TranslationTextComponent(translationKey));
    }

    /// Shorthand for style(TextStyleClass.WARNING).text(new TranslationTextComponent(translationKey):
    public CompoundText warning(String translationKey) {
        return style(TextStyleClass.WARNING).text(new TranslationTextComponent(translationKey));
    }

    /// Shorthand for style(TextStyleClass.ERROR).text(new TranslationTextComponent(translationKey):
    public CompoundText error(String translationKey) {
        return style(TextStyleClass.ERROR).text(new TranslationTextComponent(translationKey));
    }

    /// Shorthand for style(TextStyleClass.LABEL).text(new TranslationTextComponent(translationKey):
    public CompoundText label(String translationKey) {
        return style(TextStyleClass.LABEL).text(new TranslationTextComponent(translationKey));
    }

    /// Shorthand for style(TextStyleClass.OK).text(new TranslationTextComponent(translationKey):
    public CompoundText ok(String translationKey) {
        return style(TextStyleClass.OK).text(new TranslationTextComponent(translationKey));
    }

    /// Shorthand for style(TextStyleClass.NAME).text(new TranslationTextComponent(translationKey):
    public CompoundText name(String translationKey) {
        return style(TextStyleClass.NAME).text(new TranslationTextComponent(translationKey));
    }

    /// Shorthand for style(TextStyleClass.PROGRESS).text(new TranslationTextComponent(translationKey):
    public CompoundText progress(String translationKey) {
        return style(TextStyleClass.PROGRESS).text(new TranslationTextComponent(translationKey));
    }

    private CompoundText newComponent(ITextComponent cmp) {
        if (component == null) {
            component = cmp;
        } else if (component instanceof IFormattableTextComponent) {
            ((IFormattableTextComponent) component).func_230529_a_(cmp);
        } else {
            component = component.func_230532_e_().func_230529_a_(cmp);
        }
        return this;
    }

    public ITextComponent get() {
        return component;
    }
}
