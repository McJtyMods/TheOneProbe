package mcjty.theoneprobe.lib.transfer.fluid;

import net.minecraft.core.Direction;

import javax.annotation.Nullable;

public interface FluidTransferable {
	@Nullable
	IFluidHandler getFluidHandler(@Nullable Direction direction);
}
