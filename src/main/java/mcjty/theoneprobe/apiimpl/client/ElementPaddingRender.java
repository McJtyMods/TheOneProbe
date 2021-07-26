package mcjty.theoneprobe.apiimpl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public class ElementPaddingRender {

    public static void renderPadding(PoseStack stack, int x, int y, int w, int h, int color) {
        GuiComponent.fill(stack, x, y, x + w, y + h, color);
    }
}
