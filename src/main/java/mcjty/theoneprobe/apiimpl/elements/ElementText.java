package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.IElementNew;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.network.PacketBuffer;

public class ElementText implements IElementNew {

    private final String text;

    public ElementText(String text) {
        this.text = text;
    }

    public ElementText(PacketBuffer buf) {
        text = NetworkTools.readStringUTF8(buf);
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(text, x, y);
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
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_TEXT;
    }
}
