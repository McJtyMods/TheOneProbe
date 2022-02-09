package mcjty.theoneprobe.lib.transfer.fluid;

import mcjty.theoneprobe.lib.transfer.TransferUtil;
import mcjty.theoneprobe.lib.transfer.item.ItemHandlerHelper;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nonnull;
import java.util.Optional;

public class FluidUtil {

    public static String getTranslationKey(Fluid fluid) {
        String translationKey;

        if (fluid == Fluids.EMPTY) {
            translationKey = "";
        } else if (fluid == Fluids.WATER) {
            translationKey = "block.minecraft.water";
        } else if (fluid == Fluids.LAVA) {
            translationKey = "block.minecraft.lava";
        } else {
            ResourceLocation id = Registry.FLUID.getKey(fluid);
            String key = Util.makeDescriptionId("block", id);
            String translated = I18n.get(key);
            translationKey = translated.equals(key) ? Util.makeDescriptionId("fluid", id) : key;
        }

        return translationKey;
    }

    /**
     * Helper method to get the fluid contained in an itemStack
     */
    public static Optional<FluidStack> getFluidContained(@Nonnull ItemStack container)
    {
        if (!container.isEmpty())
        {
            container = ItemHandlerHelper.copyStackWithSize(container, 1);
            Optional<FluidStack> fluidContained = TransferUtil.getFluidHandlerItem(container)
                    .map(handler -> handler.drain(Integer.MAX_VALUE, true));
            if (fluidContained.isPresent() && !fluidContained.get().isEmpty())
            {
                return fluidContained;
            }
        }
        return Optional.empty();
    }

    /**
     * @param fluidStack contents used to fill the bucket.
     *                   FluidStack is used instead of Fluid to preserve fluid NBT, the amount is ignored.
     * @return a filled vanilla bucket or filled universal bucket.
     *         Returns empty itemStack if none of the enabled buckets can hold the fluid.
     */
    @Nonnull
    public static ItemStack getFilledBucket(@Nonnull FluidStack fluidStack)
    {
        Fluid fluid = fluidStack.getFluid();

        if (!fluidStack.hasTag() || fluidStack.getTag().isEmpty())
        {
            if (fluid == Fluids.WATER)
            {
                return new ItemStack(Items.WATER_BUCKET);
            }
            else if (fluid == Fluids.LAVA)
            {
                return new ItemStack(Items.LAVA_BUCKET);
            }
        }

        return new ItemStack(fluid.getBucket());
    }
}
