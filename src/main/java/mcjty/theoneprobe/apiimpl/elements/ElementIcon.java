package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementIconRender;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

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

    public ElementIcon(FriendlyByteBuf buf) {
        icon = buf.readResourceLocation();
        u = buf.readInt();
        v = buf.readInt();
        w = buf.readInt();
        h = buf.readInt();
        style = new IconStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .textureWidth(buf.readInt())
                .textureHeight(buf.readInt())
                .color(buf.readInt());
    }
    
    public IIconStyle getStyle() {
    	return style;
    }
    
    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        ElementIconRender.render(icon, graphics.pose(), x, y, w, h, u, v, style.getTextureWidth(), style.getTextureHeight(), style.getColor());
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
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(icon);
        buf.writeInt(u);
        buf.writeInt(v);
        buf.writeInt(w);
        buf.writeInt(h);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeInt(style.getTextureWidth());
        buf.writeInt(style.getTextureHeight());
        buf.writeInt(style.getColor());
    }

    @Override
    public ResourceLocation getID() {
        return TheOneProbeImp.ELEMENT_ICON;
    }
}
