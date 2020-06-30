package mcjty.theoneprobe.apiimpl.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class ElementIconRender {

    public static void render(ResourceLocation icon, MatrixStack matrixStack, int x, int y, int w, int h, int u, int v, int txtw, int txth) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (icon == null) {
            return;
        }

        if (u == -1) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(icon);
            if (sprite == null) {
                return;
            }
            Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.drawTexturedModalRect(matrixStack.getLast().getMatrix(), x, y, sprite, w, h);
        } else {
            Minecraft.getInstance().getTextureManager().bindTexture(icon);
            RenderHelper.drawTexturedModalRect(matrixStack.getLast().getMatrix(), x, y, u, v, w, h, txtw, txth);
        }
    }
}
