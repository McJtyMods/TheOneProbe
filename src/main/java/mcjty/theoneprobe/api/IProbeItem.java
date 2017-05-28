package mcjty.theoneprobe.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IProbeItem {
	boolean canWorkAsProbe(ItemStack stack, EntityPlayer player);
}
