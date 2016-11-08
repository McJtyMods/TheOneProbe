package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;

public class ElementTextRender {

    public static void render(String text, int x, int y, TextStyleClass styleClass) {
        if (styleClass != null) {
            text = Config.getTextStyle(styleClass) + text;
        }
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, text);
    }

    public static int getWidth(String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }
}
