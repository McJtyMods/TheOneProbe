package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.Cursor;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;

public class ElementText implements IElement {

    private final String text;

    public ElementText(String text) {
        this.text = text;
    }

    public ElementText(ByteBuf buf) {
        text = NetworkTools.readString(buf);
    }

    @Override
    public void render(Cursor cursor) {
        RenderHelper.renderText(Minecraft.getMinecraft(), cursor.getX(), cursor.getY(), text);
    }

    @Override
    public int getWidth() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, text);
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_TEXT;
    }
}
