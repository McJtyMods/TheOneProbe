package mcjty.theoneprobe.apiimpl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;

public class ElementIconRender {

    public static void render(ResourceLocation icon, PoseStack matrixStack, int x, int y, int w, int h, int u, int v, int txtw, int txth, int color) {
        RenderSystem.color4f(((color >> 16) & 255) / 255F, ((color >> 8) & 255) / 255F, (color & 255) / 255F, ((color >> 24) & 255) / 255F);

        if (icon == null) {
            return;
        }

        if (u == -1) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(icon);
            if (sprite == null) {
                return;
            }
            Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
            RenderHelper.drawTexturedModalRect(matrixStack.last().pose(), x, y, sprite, w, h);
        } else {
            Minecraft.getInstance().getTextureManager().bind(icon);
            RenderHelper.drawTexturedModalRect(matrixStack.last().pose(), x, y, u, v, w, h, txtw, txth);
        }
        RenderSystem.color4f(1F, 1F, 1F, 1F);
    }
}
