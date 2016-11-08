package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.ITextStyle;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.apiimpl.styles.TextStyle;
import mcjty.theoneprobe.network.NetworkTools;

public class ElementText implements IElement {

    private final String text;
    private final ITextStyle style;

    public ElementText(String text, ITextStyle style) {
        this.text = text;
        this.style = style;
    }

    public ElementText(ByteBuf buf) {
        text = NetworkTools.readStringUTF8(buf);
        style = new TextStyle()
                .styleClass(TextStyleClass.values()[buf.readByte()]);
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(text, x, y, style.getStyleClass());
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(text);
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeStringUTF8(buf, text);
        buf.writeByte(style.getStyleClass().ordinal());
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_TEXT;
    }
}
