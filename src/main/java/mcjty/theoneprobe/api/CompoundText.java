package mcjty.theoneprobe.api;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CompoundText {

    private ITextComponent component = null;

    public static CompoundText create() {
        return new CompoundText();
    }

    public CompoundText style(TextStyleClass style) {
        ITextComponent cmp = new StringTextComponent(style.toString());
        return newComponent(cmp);
    }

    public CompoundText text(ITextComponent cmp) {
        return newComponent(cmp);
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
