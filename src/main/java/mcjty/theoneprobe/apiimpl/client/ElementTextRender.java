package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

public class ElementTextRender {

    public static void render(String text, int x, int y) {
        if (text.contains("{=")) {
            for (TextStyleClass styleClass : Config.textStyleClasses.keySet()) {
                String replacement = Config.getTextStyle(styleClass);
                text = StringUtils.replace(text, styleClass.toString(), replacement);
            }
        }
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, text);
    }

    public static int getWidth(String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }
}
