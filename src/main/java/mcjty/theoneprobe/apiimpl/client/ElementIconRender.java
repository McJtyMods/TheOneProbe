package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ElementIconRender {

    public static void render(ResourceLocation icon, int x, int y, int w, int h, int u, int v) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (icon == null) {
            return;
        }

        if (u == -1) {
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(icon.toString());
            if (sprite == null) {
                return;
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.drawTexturedModalRect(x, y, sprite, w, h);
        } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(icon);

            int txtw = 256;
            int txth = 256;
            IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
            try {
                IResource resource = resourceManager.getResource(icon);
                BufferedImage bufferedimage = TextureUtil.readBufferedImage(resource.getInputStream());
                txtw = bufferedimage.getWidth();
                txth = bufferedimage.getHeight();
            } catch (IOException e) {
                // Eat exception. Not much sensible we can do here anyway
            }
            RenderHelper.drawTexturedModalRect(x, y, u, v, w, h, txtw, txth);
        }
    }
}
