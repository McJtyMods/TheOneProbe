package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.apiimpl.IconStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class ElementIcon implements IElement {

    private final ResourceLocation icon;
    private final int u;
    private final int v;
    private final int w;
    private final int h;
    private final IIconStyle style;

    public ElementIcon(ResourceLocation icon, int u, int v, int w, int h, IIconStyle style) {
        this.icon = icon;
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        this.style = style;
    }

    public ElementIcon(ByteBuf buf) {
        icon = new ResourceLocation(NetworkTools.readString(buf), NetworkTools.readString(buf));
        u = buf.readInt();
        v = buf.readInt();
        w = buf.readInt();
        h = buf.readInt();
        style = new IconStyle()
                .width(buf.readInt())
                .height(buf.readInt());
    }

    @Override
    public void render(int x, int y) {
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
            RenderHelper.drawTexturedModalRect(x, y, u, v, w, h);
        }
    }

    @Override
    public int getWidth() {
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return style.getHeight();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, icon.getResourceDomain());
        NetworkTools.writeString(buf, icon.getResourcePath());
        buf.writeInt(u);
        buf.writeInt(v);
        buf.writeInt(w);
        buf.writeInt(h);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ICON;
    }
}
