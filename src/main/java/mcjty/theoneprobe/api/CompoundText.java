package mcjty.theoneprobe.api;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

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

    private CompoundText newComponent(ITextComponent cmp) {
        if (component == null) {
            component = cmp;
        } else {
            component.appendSibling(cmp);
        }
        return this;
    }

    public ITextComponent get() {
        return component;
    }
}
