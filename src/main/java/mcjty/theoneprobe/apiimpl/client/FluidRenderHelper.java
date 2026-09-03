package mcjty.theoneprobe.apiimpl.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

public final class FluidRenderHelper {

    private FluidRenderHelper() {
    }

    public static TextureAtlasSprite getStillSprite(FluidStack stack) {
        return getFluidModel(stack).stillMaterial().sprite();
    }

    public static int getTintColor(FluidStack stack) {
        FluidTintSource tintSource = getFluidModel(stack).fluidTintSource();
        return tintSource == null ? -1 : tintSource.colorAsStack(stack);
    }

    private static FluidModel getFluidModel(FluidStack stack) {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet()
                .get(stack.getFluid().defaultFluidState());
    }
}
