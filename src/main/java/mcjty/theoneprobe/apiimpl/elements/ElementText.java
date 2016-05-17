package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;

public class ElementText implements Element {

    private final String text;

    public ElementText(String text) {
        this.text = text;
    }

    @Override
    public void render(Cursor cursor) {
        int w = RenderHelper.renderText(Minecraft.getMinecraft(), cursor.getX(), cursor.getY(), text);
        cursor.addX(w);
        cursor.updateMaxY(10);
    }
}
