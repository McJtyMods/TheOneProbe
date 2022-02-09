package mcjty.theoneprobe.lib.transfer.item;

import net.minecraft.core.Direction;

import javax.annotation.Nullable;

public interface ItemTransferable {
	@Nullable
	IItemHandler getItemHandler(@Nullable Direction direction);
}
