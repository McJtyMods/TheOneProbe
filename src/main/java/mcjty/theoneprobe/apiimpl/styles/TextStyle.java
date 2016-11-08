package mcjty.theoneprobe.apiimpl.styles;

import mcjty.theoneprobe.api.ITextStyle;
import mcjty.theoneprobe.api.TextStyleClass;

public class TextStyle implements ITextStyle {

    private TextStyleClass styleClass;

    @Override
    public ITextStyle styleClass(TextStyleClass styleClass) {
        this.styleClass = styleClass;
        return this;
    }

    @Override
    public TextStyleClass getStyleClass() {
        return styleClass;
    }
}
