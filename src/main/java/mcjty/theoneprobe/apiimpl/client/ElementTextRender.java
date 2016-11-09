package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.Set;

public class ElementTextRender {

    public static void render(String text, int x, int y) {
        if (text.contains("{=")) {
            Set<TextStyleClass> stylesNeedingContext = EnumSet.noneOf(TextStyleClass.class);
            TextStyleClass context = null;
            for (TextStyleClass styleClass : Config.textStyleClasses.keySet()) {
                if (text.contains(styleClass.toString())) {
                    String replacement = Config.getTextStyle(styleClass);
                    if ("context".equals(replacement)) {
                        stylesNeedingContext.add(styleClass);
                    } else if (context == null) {
                        context = styleClass;
                        text = StringUtils.replace(text, styleClass.toString(), replacement);
                    } else {
                        text = StringUtils.replace(text, styleClass.toString(), replacement);
                    }
                }
            }
            if (context != null) {
                for (TextStyleClass styleClass : stylesNeedingContext) {
                    String replacement = Config.getTextStyle(context);
                    text = StringUtils.replace(text, styleClass.toString(), replacement);
                }
            }
        }
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, text);
    }

    public static int getWidth(String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }
}
