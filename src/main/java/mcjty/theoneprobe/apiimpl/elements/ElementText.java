package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.IElementNew;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class ElementText implements IElementNew {

    private final String text;
    private final ITextComponent textComponent;

    public ElementText(String text) {
        this.text = text;
        this.textComponent = null;
    }

    public ElementText(ITextComponent text) {
        this.text = "";
        this.textComponent = text;
    }

    public ElementText(PacketBuffer buf) {
        text = NetworkTools.readStringUTF8(buf);
        if (buf.readBoolean()) {
            textComponent = buf.readTextComponent();
        } else {
            textComponent = null;
        }
    }

    @Override
    public void render(int x, int y) {
        if (textComponent != null) {
            ElementTextRender.render(textComponent, x, y);
        } else {
            ElementTextRender.render(text, x, y);
        }
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
    public void toBytes(PacketBuffer buf) {
        NetworkTools.writeStringUTF8(buf, text);
        if (textComponent != null) {
            buf.writeBoolean(true);
            buf.writeTextComponent(textComponent);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_TEXT;
    }
}
