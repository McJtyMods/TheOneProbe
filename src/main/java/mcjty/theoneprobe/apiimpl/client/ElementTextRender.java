package mcjty.theoneprobe.apiimpl.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.Set;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;

public class ElementTextRender {

	public static void render(ITextComponent text, MatrixStack matrixStack, int x, int y, boolean legacy) {
		if (legacy) {
            render(text, matrixStack, x, y);
        } else {
            RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, text);
        }
	}
	
    public static void render(ITextComponent text, MatrixStack matrixStack, int x, int y) {
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, stylifyString(text));
    }

    private static String stylifyString(ITextComponent text) {
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
                middle = I18n.format(middle).trim();
                String right = text.substring(end+2);
                text = left + middle + right;
            } else {
                break;
            }
        }
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
        return text;
    }

    public static int getLegacyWidth(ITextComponent text) {
        return Minecraft.getInstance().fontRenderer.getStringWidth(stylifyString(text));
    }
    
    public static int getWidth(ITextComponent text) {
    	return Minecraft.getInstance().fontRenderer.func_243245_a(text.func_241878_f());
    }
    
    public static int getHeight() {
    	return Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
    }
}
