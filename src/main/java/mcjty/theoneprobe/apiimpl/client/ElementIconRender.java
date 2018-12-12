package mcjty.theoneprobe.apiimpl.client;

import com.mojang.blaze3d.platform.GlStateManager;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

public class ElementIconRender {

    public static void render(Identifier icon, int x, int y, int w, int h, int u, int v, int txtw, int txth) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (icon == null) {
            return;
        }

        if (u == -1) {
            Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(icon.toString());
            if (sprite == null) {
                return;
            }
            MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
            RenderHelper.drawTexturedModalRect(x, y, sprite, w, h);
        } else {
            MinecraftClient.getInstance().getTextureManager().bindTexture(icon);
            RenderHelper.drawTexturedModalRect(x, y, u, v, w, h, txtw, txth);
        }
    }
}
