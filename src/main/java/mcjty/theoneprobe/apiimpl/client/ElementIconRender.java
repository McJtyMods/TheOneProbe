package mcjty.theoneprobe.apiimpl.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;

public class ElementIconRender {

    public static void render(Identifier icon, GuiGraphics graphics, int x, int y, int w, int h, int u, int v, int txtw, int txth, int color) {
        if (icon == null) {
            return;
        }

        if (u == -1) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager()
                    .getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(icon);
            if (sprite == null) {
                return;
            }
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, w, h, color);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, icon, x, y, u, v, w, h, txtw, txth, color);
        }
    }
}
