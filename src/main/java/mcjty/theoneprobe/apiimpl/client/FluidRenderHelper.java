package mcjty.theoneprobe.apiimpl.client;

import net.minecraft.client.Minecraft;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import mcjty.theoneprobe.api.FluidStack;

public final class FluidRenderHelper {

    private FluidRenderHelper() {
    }

    public static TextureAtlasSprite getStillSprite(FluidStack stack) {
        return getFluidModel(stack).stillMaterial().sprite();
    }

    public static int getTintColor(FluidStack stack) {
        return FluidVariantRendering.getColor(stack.variant());
    }

    private static FluidModel getFluidModel(FluidStack stack) {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet()
                .get(stack.getFluid().defaultFluidState());
    }
}
