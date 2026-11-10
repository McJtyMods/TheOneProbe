package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.Color;
import mcjty.theoneprobe.api.FluidStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementIconRender;
import mcjty.theoneprobe.apiimpl.client.FluidRenderHelper;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ElementFluid implements IElement {

    private final FluidStack fluid;
    private final IIconStyle style;

    public ElementFluid(FluidStack fluid, IIconStyle style) {
        this.fluid = fluid;
        this.style = style;
    }

    public ElementFluid(RegistryFriendlyByteBuf buf) {
        fluid = FluidStack.STREAM_CODEC.decode(buf);
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
        int tintColor = FluidVariantRendering.getColor(fluid.variant());
        ResourceLocation stillTexture = FluidRenderHelper.getStillSprite(fluid).contents().name().withPrefix("textures/").withSuffix(".png");
        Color color = new Color(tintColor);
        ElementIconRender.render(stillTexture, graphics.pose(), x, y, 16, 16, -1, -1, style.getTextureWidth(), style.getTextureHeight(), color.getRGB());
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
        FluidStack.STREAM_CODEC.encode(buf, fluid);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeInt(style.getTextureWidth());
        buf.writeInt(style.getTextureHeight());
        buf.writeInt(style.getColor());
    }

    @Override
    public ResourceLocation getID() {
        return TheOneProbeImp.ELEMENT_FLUID;
    }
}
