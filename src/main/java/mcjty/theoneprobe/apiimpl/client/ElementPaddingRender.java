package mcjty.theoneprobe.apiimpl.client;

import net.minecraft.client.gui.GuiGraphics;

public class ElementPaddingRender {

    public static void renderPadding(GuiGraphics graphics, int x, int y, int w, int h, int color) {
        graphics.fill(x, y, x + w, y + h, color);
    }
}
