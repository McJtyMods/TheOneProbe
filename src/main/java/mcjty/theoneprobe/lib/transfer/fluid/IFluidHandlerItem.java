package mcjty.theoneprobe.lib.transfer.fluid;

import net.minecraft.world.item.ItemStack;

public interface IFluidHandlerItem extends IFluidHandler {
	ItemStack getContainer();
}
