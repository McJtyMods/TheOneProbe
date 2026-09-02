package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.Color;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementIconRender;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.fluids.FluidStack;

import static mcjty.theoneprobe.apiimpl.client.FluidRenderHelper.getStillSprite;
import static mcjty.theoneprobe.apiimpl.client.FluidRenderHelper.getTintColor;

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
    public void render(GuiGraphicsExtractor graphics, int x, int y) {
        Color color = new Color(getTintColor(fluid));
        ElementIconRender.render(getStillSprite(fluid), graphics, x, y, 16, 16, color.getRGB());
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
    public Identifier getID() {
        return TheOneProbeImp.ELEMENT_FLUID;
    }
}
