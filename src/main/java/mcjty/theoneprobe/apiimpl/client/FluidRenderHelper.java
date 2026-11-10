package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public final class FluidRenderHelper {

    private FluidRenderHelper() {}

    public static TextureAtlasSprite getStillSprite(FluidStack stack) {
        return FluidVariantRendering.getSprite(stack.variant());
    }

    public static int getTintColor(FluidStack stack) {
        return FluidVariantRendering.getColor(stack.variant());
    }
}
