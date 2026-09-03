package mcjty.theoneprobe.apiimpl.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.fluids.FluidStack;

public final class FluidRenderHelper {

    private FluidRenderHelper() {
    }

    private static FluidModel getModel(FluidStack stack) {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet()
                .get(stack.getFluid().defaultFluidState());
    }

    public static TextureAtlasSprite getStillSprite(FluidStack stack) {
        return getModel(stack).stillMaterial().sprite();
    }

    public static int getTintColor(FluidStack stack) {
        var tintSource = getModel(stack).fluidTintSource();
        return tintSource == null ? 0xffffffff : tintSource.colorAsStack(stack);
    }
}
