package mcjty.theoneprobe.rendering;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.network.ThrowableIdentity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderHelper {
    public static float rot = 0.0f;

    public static void renderEntity(Entity entity, int xPos, int yPos, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(xPos + 8, yPos + 24, 50F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135F, 0.0F, 1.0F, 0.0F);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
//        entity.renderYawOffset = entity.rotationYaw = entity.prevRotationYaw = entity.prevRotationYawHead = entity.rotationYawHead = 0;//this.rotateTurret;
        entity.rotationPitch = 0.0F;
        GlStateManager.translate(0.0F, (float) entity.getYOffset() + (entity instanceof EntityHanging ? 0.5F : 0.0F), 0.0F);
        Minecraft.getMinecraft().getRenderManager().playerViewY = 180F;
        try {
            Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        } catch (Exception e) {
            TheOneProbe.setup.getLogger().error("Error rendering entity!", e);
        }
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        GlStateManager.disableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableDepth();
        GlStateManager.disableColorMaterial();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static boolean renderObject(Minecraft mc, int x, int y, Object itm, boolean highlight) {
        if (itm instanceof Entity) {
            renderEntity((Entity) itm, x, y, 10);
            return true;
        }
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        return renderObject(mc, itemRender, x, y, itm, highlight, 200);
    }

    public static boolean renderObject(Minecraft mc, RenderItem itemRender, int x, int y, Object itm, boolean highlight, float lvl) {
        itemRender.zLevel = lvl;

        if (itm == null) {
            return renderItemStack(mc, itemRender, null, x, y, "", highlight);
        }
        if (itm instanceof Item) {
            return renderItemStack(mc, itemRender, new ItemStack((Item) itm, 1), x, y, "", highlight);
        }
        if (itm instanceof Block) {
            return renderItemStack(mc, itemRender, new ItemStack((Block) itm, 1), x, y, "", highlight);
        }
        if (itm instanceof ItemStack) {
            return renderItemStackWithCount(mc, itemRender, (ItemStack) itm, x, y, highlight);
        }
        if (itm instanceof TextureAtlasSprite) {
            return renderIcon(mc, itemRender, (TextureAtlasSprite) itm, x, y, highlight);
        }
        return renderItemStack(mc, itemRender, ItemStack.EMPTY, x, y, "", highlight);
    }

    public static boolean renderIcon(Minecraft mc, RenderItem itemRender, TextureAtlasSprite itm, int xo, int yo, boolean highlight) {
        //itemRender.renderIcon(xo, yo, itm, 16, 16); //TODO: Make
        return true;
    }

    public static boolean renderItemStackWithCount(Minecraft mc, RenderItem itemRender, ItemStack itm, int xo, int yo, boolean highlight) {
        if (itm.getCount() <= 1) {
            return renderItemStack(mc, itemRender, itm, xo, yo, "", highlight);
        } else {
            return renderItemStack(mc, itemRender, itm, xo, yo, "" + itm.getCount(), highlight);
        }
    }

    public static boolean renderItemStack(Minecraft mc, RenderItem itemRender, ItemStack itm, int x, int y, String txt, boolean highlight) {
        GlStateManager.color(1F, 1F, 1F);

        boolean rc = false;
        if (highlight) {
            GlStateManager.disableLighting();
            drawVerticalGradientRect(x, y, x + 16, y + 16, 0x80ffffff, 0xffffffff);
        }
        if (!itm.isEmpty() && itm.getItem() != null) {
            rc = true;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 32.0F);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            short short1 = 240;
            short short2 = 240;
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
            itemRender.renderItemAndEffectIntoGUI(itm, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, itm, x, y, txt);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
        }

        return rc;
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     * x2 and y2 are not included.
     */
    public static void drawVerticalGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
//        this.zLevel = 300.0F;
        float zLevel = 0.0f;

        float f = (color1 >> 24 & 255) / 255.0F;
        float f1 = (color1 >> 16 & 255) / 255.0F;
        float f2 = (color1 >> 8 & 255) / 255.0F;
        float f3 = (color1 & 255) / 255.0F;
        float f4 = (color2 >> 24 & 255) / 255.0F;
        float f5 = (color2 >> 16 & 255) / 255.0F;
        float f6 = (color2 >> 8 & 255) / 255.0F;
        float f7 = (color2 & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x2, y1, zLevel).color(f1, f2, f3, f).endVertex();
        buffer.pos(x1, y1, zLevel).color(f1, f2, f3, f).endVertex();
        buffer.pos(x1, y2, zLevel).color(f5, f6, f7, f4).endVertex();
        buffer.pos(x2, y2, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    /**
     * Draws a rectangle with a horizontal gradient between the specified colors.
     * x2 and y2 are not included.
     */
    public static void drawHorizontalGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
//        this.zLevel = 300.0F;
        float zLevel = 0.0f;

        float f = (color1 >> 24 & 255) / 255.0F;
        float f1 = (color1 >> 16 & 255) / 255.0F;
        float f2 = (color1 >> 8 & 255) / 255.0F;
        float f3 = (color1 & 255) / 255.0F;
        float f4 = (color2 >> 24 & 255) / 255.0F;
        float f5 = (color2 >> 16 & 255) / 255.0F;
        float f6 = (color2 >> 8 & 255) / 255.0F;
        float f7 = (color2 & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x1, y1, zLevel).color(f1, f2, f3, f).endVertex();
        buffer.pos(x1, y2, zLevel).color(f1, f2, f3, f).endVertex();
        buffer.pos(x2, y2, zLevel).color(f5, f6, f7, f4).endVertex();
        buffer.pos(x2, y1, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawHorizontalLine(int x1, int y1, int x2, int color) {
        Gui.drawRect(x1, y1, x2, y1 + 1, color);
    }

    public static void drawVerticalLine(int x1, int y1, int y2, int color) {
        Gui.drawRect(x1, y1, x1 + 1, y2, color);
    }

    // Draw a small triangle. x,y is the coordinate of the left point
    public static void drawLeftTriangle(int x, int y, int color) {
        drawVerticalLine(x, y, y, color);
        drawVerticalLine(x + 1, y - 1, y + 1, color);
        drawVerticalLine(x + 2, y - 2, y + 2, color);
    }

    // Draw a small triangle. x,y is the coordinate of the right point
    public static void drawRightTriangle(int x, int y, int color) {
        drawVerticalLine(x, y, y, color);
        drawVerticalLine(x - 1, y - 1, y + 1, color);
        drawVerticalLine(x - 2, y - 2, y + 2, color);
    }

    // Draw a small triangle. x,y is the coordinate of the top point
    public static void drawUpTriangle(int x, int y, int color) {
        drawHorizontalLine(x, y, x, color);
        drawHorizontalLine(x - 1, y + 1, x + 1, color);
        drawHorizontalLine(x - 2, y + 2, x + 2, color);
    }

    // Draw a small triangle. x,y is the coordinate of the bottom point
    public static void drawDownTriangle(int x, int y, int color) {
        drawHorizontalLine(x, y, x, color);
        drawHorizontalLine(x - 1, y - 1, x + 1, color);
        drawHorizontalLine(x - 2, y - 2, x + 2, color);
    }

    /**
     * Draw a button box. x2 and y2 are not included.
     */
    public static void drawFlatButtonBox(int x1, int y1, int x2, int y2, int bright, int average, int dark) {
        drawBeveledBox(x1, y1, x2, y2, bright, dark, average);
    }

    /**
     * Draw a button box. x2 and y2 are not included.
     */
    public static void drawFlatButtonBoxGradient(int x1, int y1, int x2, int y2, int bright, int average1, int average2, int dark) {
        drawVerticalGradientRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, average2, average1);
        drawHorizontalLine(x1, y1, x2 - 1, bright);
        drawVerticalLine(x1, y1, y2 - 1, bright);
        drawVerticalLine(x2 - 1, y1, y2 - 1, dark);
        drawHorizontalLine(x1, y2 - 1, x2, dark);
    }

    /**
     * Draw a beveled box. x2 and y2 are not included.
     */
    public static void drawBeveledBox(int x1, int y1, int x2, int y2, int topleftcolor, int botrightcolor, int fillcolor) {
        if (fillcolor != -1) {
            Gui.drawRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, fillcolor);
        }
        drawHorizontalLine(x1, y1, x2 - 1, topleftcolor);
        drawVerticalLine(x1, y1, y2 - 1, topleftcolor);
        drawVerticalLine(x2 - 1, y1, y2 - 1, botrightcolor);
        drawHorizontalLine(x1, y2 - 1, x2, botrightcolor);
    }

    /**
     * Draw a thick beveled box. x2 and y2 are not included.
     */
    public static void drawThickBeveledBox(int x1, int y1, int x2, int y2, int thickness, int topleftcolor, int botrightcolor, int fillcolor) {
        if (fillcolor != -1) {
            Gui.drawRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, fillcolor);
        }
        Gui.drawRect(x1, y1, x2 - 1, y1 + thickness, topleftcolor);
        Gui.drawRect(x1, y1, x1 + thickness, y2 - 1, topleftcolor);
        Gui.drawRect(x2 - thickness, y1, x2, y2 - 1, botrightcolor);
        Gui.drawRect(x1, y2 - thickness, x2, y2, botrightcolor);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, int twidth, int theight) {
        float zLevel = 0.01f;
        float f = (1.0f/twidth);
        float f1 = (1.0f/theight);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos((x + 0), (y + height), zLevel).tex(((u + 0) * f), ((v + height) * f1)).endVertex();
        buffer.pos((x + width), (y + height), zLevel).tex(((u + width) * f), ((v + height) * f1)).endVertex();
        buffer.pos((x + width), (y + 0), zLevel).tex(((u + width) * f), ((v + 0) * f1)).endVertex();
        buffer.pos((x + 0), (y + 0), zLevel).tex(((u + 0) * f), ((v + 0) * f1)).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        float zLevel = 0.01f;
        float f = (1/256.0f);
        float f1 = (1/256.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos((x + 0), (y + height), zLevel).tex(((u + 0) * f), ((v + height) * f1)).endVertex();
        buffer.pos((x + width), (y + height), zLevel).tex(((u + width) * f), ((v + height) * f1)).endVertex();
        buffer.pos((x + width), (y + 0), zLevel).tex(((u + width) * f), ((v + 0) * f1)).endVertex();
        buffer.pos((x + 0), (y + 0), zLevel).tex(((u + 0) * f), ((v + 0) * f1)).endVertex();
        tessellator.draw();
    }

    public static void drawTexturedModalRect(int x, int y, TextureAtlasSprite sprite, int width, int height) {
        float zLevel = 0.01f;
        float f = (1/256.0f);
        float f1 = (1/256.0f);

        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();
        float u2 = sprite.getMaxU();
        float v2 = sprite.getMaxV();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos((x + 0), (y + height), zLevel).tex(u1, v1).endVertex();
        buffer.pos((x + width), (y + height), zLevel).tex(u1, v2).endVertex();
        buffer.pos((x + width), (y + 0), zLevel).tex(u2, v2).endVertex();
        buffer.pos((x + 0), (y + 0), zLevel).tex(u2, v1).endVertex();
        tessellator.draw();
    }

    public static void renderBillboardQuadBright(double scale) {
        int brightness = 240;
        int b1 = brightness >> 16 & 65535;
        int b2 = brightness & 65535;
        GlStateManager.pushMatrix();
        RenderHelper.rotateToPlayer();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        buffer.pos(-scale, -scale, 0.0D).tex(0.0D, 0.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
        buffer.pos(-scale, scale, 0.0D).tex(0.0D, 1.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
        buffer.pos(scale, scale, 0.0D).tex(1.0D, 1.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
        buffer.pos(scale, -scale, 0.0D).tex(1.0D, 0.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
    }

    public static void renderBillboardQuad(double scale) {
        GlStateManager.pushMatrix();

        rotateToPlayer();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-scale, -scale, 0).tex(0, 0).endVertex();
        buffer.pos(-scale, +scale, 0).tex(0, 1).endVertex();

        buffer.pos(+scale, +scale, 0).tex(1, 1).endVertex();

        buffer.pos(+scale, -scale, 0).tex(1, 0).endVertex();

        tessellator.draw();
        GlStateManager.popMatrix();
    }

    public static void renderBillboardQuadWithRotation(float rot, double scale) {
        GlStateManager.pushMatrix();

        rotateToPlayer();

        GlStateManager.rotate(rot, 0, 0, 1);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-scale, -scale, 0).tex(0, 0).endVertex();
        buffer.pos(-scale, +scale, 0).tex(0, 1).endVertex();

        buffer.pos(+scale, +scale, 0).tex(1, 1).endVertex();

        buffer.pos(+scale, -scale, 0).tex(1, 0).endVertex();

        tessellator.draw();
        GlStateManager.popMatrix();
    }

    public static void rotateToPlayer() {
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
    }

    /**
     * Draw a beam with some thickness.
     *
     * @param S
     * @param E
     * @param P
     * @param width
     */
    public static void drawBeam(Vector S, Vector E, Vector P, float width) {
        Vector PS = Sub(S, P);
        Vector SE = Sub(E, S);

        Vector normal = Cross(PS, SE);
        normal = normal.normalize();

        Vector half = Mul(normal, width);
        Vector p1 = Add(S, half);
        Vector p2 = Sub(S, half);
        Vector p3 = Add(E, half);
        Vector p4 = Sub(E, half);

        drawQuad(Tessellator.getInstance(), p1, p3, p4, p2);
    }

    private static void drawQuad(Tessellator tessellator, Vector p1, Vector p2, Vector p3, Vector p4) {
        int brightness = 240;
        int b1 = brightness >> 16 & 65535;
        int b2 = brightness & 65535;

        BufferBuilder buffer = tessellator.getBuffer();
        buffer.pos(p1.getX(), p1.getY(), p1.getZ()).tex(0.0D, 0.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
        buffer.pos(p2.getX(), p2.getY(), p2.getZ()).tex(1.0D, 0.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
        buffer.pos(p3.getX(), p3.getY(), p3.getZ()).tex(1.0D, 1.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
        buffer.pos(p4.getX(), p4.getY(), p4.getZ()).tex(0.0D, 1.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
    }

    public static boolean renderItemStack(Minecraft mc, RenderItem itemRender, ItemStack itm, int x, int y, String txt) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        boolean rc = true;
        if (!itm.isEmpty() && itm.getItem() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 32.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            short short1 = 240;
            short short2 = 240;
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
            try {
                itemRender.renderItemAndEffectIntoGUI(itm, x, y);
                renderItemOverlayIntoGUI(mc.fontRenderer, itm, x, y, txt, txt.length() - 2);
            } catch (Exception e) {
                ThrowableIdentity.registerThrowable(e);
                rc = false; // Report error
            }
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
        }

        return rc;
    }

    /**
     * Renders the stack size and/or damage bar for the given ItemStack.
     */
    public static void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text,
                                                int scaled) {
        if (!stack.isEmpty()) {
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? String.valueOf(stack.getCount()) : text;
                if (text == null && stack.getCount() < 1) {
                    s = TextFormatting.RED + String.valueOf(stack.getCount());
                }

                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                if (scaled >= 2) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(.5f, .5f, .5f);
                    fr.drawStringWithShadow(s, ((xPosition + 19 - 2) * 2 - 1 - fr.getStringWidth(s)), yPosition * 2 + 24, 16777215);
                    GlStateManager.popMatrix();
                } else if (scaled == 1) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(.75f, .75f, .75f);
                    fr.drawStringWithShadow(s, ((xPosition - 2) * 1.34f + 24 - fr.getStringWidth(s)), yPosition * 1.34f + 14, 16777215);
                    GlStateManager.popMatrix();
                } else {
                    fr.drawStringWithShadow(s, (xPosition + 19 - 2 - fr.getStringWidth(s)), (yPosition + 6 + 3), 16777215);
                }
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                // Fixes opaque cooldown overlay a bit lower
                // TODO: check if enabled blending still screws things up down the line.
                GlStateManager.enableBlend();
            }

            if (stack.getItem().showDurabilityBar(stack)) {
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int j = (int) Math.round(13.0D - health * 13.0D);
                int i = (int) Math.round(255.0D - health * 255.0D);
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder vertexbuffer = tessellator.getBuffer();
                draw(vertexbuffer, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                draw(vertexbuffer, xPosition + 2, yPosition + 13, 12, 1, (255 - i) / 4, 64, 0, 255);
                draw(vertexbuffer, xPosition + 2, yPosition + 13, j, 1, 255 - i, i, 0, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }

            EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
            float f = entityplayersp == null ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());

            if (f > 0.0F) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder vertexbuffer1 = tessellator1.getBuffer();
                draw(vertexbuffer1, xPosition, yPosition + (int)Math.floor(16.0F * (1.0F - f)), 16, (int)Math.ceil(16.0F * f), 255, 255, 255, 127);
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }

    /**
     * Draw with the WorldRenderer
     */
    private static void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((x + 0), (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((x + 0), (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((x + width), (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((x + width), (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }


    public static int renderText(Minecraft mc, int x, int y, String txt) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableLighting();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        int width = mc.fontRenderer.getStringWidth(txt);
        mc.fontRenderer.drawStringWithShadow(txt, x, y, 16777215);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        // Fixes opaque cooldown overlay a bit lower
        // TODO: check if enabled blending still screws things up down the line.
        GlStateManager.enableBlend();


        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();

        return width;
    }

    public static class Vector {
        public final float x;
        public final float y;
        public final float z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public float norm() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        public Vector normalize() {
            float n = norm();
            return new Vector(x / n, y / n, z / n);
        }
    }

    private static Vector Cross(Vector a, Vector b) {
        float x = a.y * b.z - a.z * b.y;
        float y = a.z * b.x - a.x * b.z;
        float z = a.x * b.y - a.y * b.x;
        return new Vector(x, y, z);
    }

    private static Vector Sub(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    private static Vector Add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    private static Vector Mul(Vector a, float f) {
        return new Vector(a.x * f, a.y * f, a.z * f);
    }


}
