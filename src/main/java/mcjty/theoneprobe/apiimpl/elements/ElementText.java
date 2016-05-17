package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;

public class ElementText implements Element {

    private final String text;

    public ElementText(String text) {
        this.text = text;
    }

    public ElementText(ByteBuf buf) {
        text = NetworkTools.readString(buf);
    }

    @Override
    public void render(Cursor cursor) {
        int w = RenderHelper.renderText(Minecraft.getMinecraft(), cursor.getX(), cursor.getY(), text);
        cursor.addX(w);
        cursor.updateMaxY(10);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, text);
    }

    @Override
    public ElementType getType() {
        return ElementType.TEXT;
    }
}
