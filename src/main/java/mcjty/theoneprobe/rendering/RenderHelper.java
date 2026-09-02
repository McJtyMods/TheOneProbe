package mcjty.theoneprobe.rendering;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.network.ThrowableIdentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderHelper {

    public static float rot = 0.0f;

    public static void renderEntity(Entity entity, GuiGraphicsExtractor graphics, int x, int y, int width, int height, float scale) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            EntityRenderState state = dispatcher.extractEntity(entity, 1.0f);
            Quaternionf rotation = new Quaternionf().rotateZ((float) Math.PI).rotateY((float) Math.toRadians(rot));
            Vector3f offset = new Vector3f(0.0f, state.boundingBoxHeight / 2.0f, 0.0f);
            graphics.entity(state, scale, offset, rotation, null, x, y, x + width, y + height);
        } catch (Exception e) {
            TheOneProbe.logger.error("Error rendering entity!", e);
        }
    }

    public static void drawHorizontalLine(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int color) {
        graphics.fill(x1, y1, x2, y1 + 1, color);
    }

    public static void drawVerticalLine(GuiGraphicsExtractor graphics, int x1, int y1, int y2, int color) {
        graphics.fill(x1, y1, x1 + 1, y2, color);
    }

    public static void drawThickBeveledBox(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, int thickness,
                                            int topLeftColor, int bottomRightColor, int fillColor) {
        if (fillColor != -1) {
            graphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, fillColor);
        }
        graphics.fill(x1, y1, x2 - 1, y1 + thickness, topLeftColor);
        graphics.fill(x1, y1, x1 + thickness, y2 - 1, topLeftColor);
        graphics.fill(x2 - thickness, y1, x2, y2 - 1, bottomRightColor);
        graphics.fill(x1, y2 - thickness, x2, y2, bottomRightColor);
    }

    public static boolean renderItemStack(Minecraft mc, ItemStack stack, GuiGraphicsExtractor graphics,
                                          int x, int y, String amount) {
        if (stack.isEmpty()) {
            return true;
        }
        try {
            graphics.item(stack, x, y, x * y * 31);
            graphics.itemDecorations(mc.font, stack, x, y, amount);
            return true;
        } catch (Exception e) {
            ThrowableIdentity.registerThrowable(e);
            return false;
        }
    }

    public static int renderText(Minecraft mc, GuiGraphicsExtractor graphics, int x, int y, String text) {
        graphics.text(mc.font, text, x, y, 0xffffffff);
        return mc.font.width(text);
    }

    public static int renderText(Minecraft mc, GuiGraphicsExtractor graphics, int x, int y, Component text) {
        graphics.text(mc.font, text, x, y, 0xffffffff);
        return mc.font.width(text.getVisualOrderText());
    }
}
