package mcjty.theoneprobe.apiimpl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import java.util.EnumSet;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;

public class ElementTextRender {

	public static void render(Component text, GuiGraphics graphics, int x, int y, boolean legacy) {
		if (legacy) {
            render(text, graphics, x, y);
        } else {
            RenderHelper.renderText(Minecraft.getInstance(), graphics, x, y, text);
        }
	}
	
    public static void render(Component text, GuiGraphics graphics, int x, int y) {
        RenderHelper.renderText(Minecraft.getInstance(), graphics, x, y, stylifyString(text));
    }

    private static String stylifyString(Component text) {
        return stylifyString(text.getString());
    }

    private static String stylifyString(String text) {
        while (text.contains(STARTLOC) && text.contains(ENDLOC)) {
            int start = text.indexOf(STARTLOC);
            int end = text.indexOf(ENDLOC);
            if (start < end) {
                // Translation is needed
                String left = text.substring(0, start);
                String middle = text.substring(start + 2, end);
                middle = I18n.get(middle).trim();
                String right = text.substring(end+2);
                text = left + middle + right;
            } else {
                break;
            }
        }
        if (text.contains("{=")) {
            var stylesNeedingContext = EnumSet.noneOf(TextStyleClass.class);
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
        return text;
    }

    public static int getLegacyWidth(Component text) {
        return Minecraft.getInstance().font.width(stylifyString(text));
    }
    
    public static int getWidth(Component text) {
    	return Minecraft.getInstance().font.width(text.getVisualOrderText());
    }
    
    public static int getHeight() {
    	return Minecraft.getInstance().font.lineHeight;
    }
}
