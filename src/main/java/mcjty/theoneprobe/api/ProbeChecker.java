package mcjty.theoneprobe.api;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ProbeChecker {

	public static String PROBETAG = "theoneprobe";
	public static String PROBETAG_HAND = "theoneprobe_hand";

	public static boolean canWorkAsProbe(ItemStack stack, EntityPlayer player, String nbtProbeTagName) {
		if (stack == null) {
			return false;
		}
		if (stack.getItem() instanceof IProbeItem) {
			return ((IProbeItem) stack.getItem()).canWorkAsProbe(stack, player);
		}
		return stack.getTagCompound() != null && stack.getTagCompound().hasKey(nbtProbeTagName);
	}

	public static boolean hasAProbeSomewhere(EntityPlayer player) {
		return hasProbeInHand(player, EnumHand.MAIN_HAND) || hasProbeInHand(player, EnumHand.OFF_HAND) || hasProbeInHelmet(player)
			|| hasProbeInBauble(player);
	}

	public static boolean hasProbeInHand(EntityPlayer player, EnumHand hand) {
		return canWorkAsProbe(player.getHeldItem(hand), player, PROBETAG_HAND);
	}

	public static boolean hasProbeInHelmet(EntityPlayer player) {
		return canWorkAsProbe(player.inventory.armorInventory[3], player, PROBETAG);
	}

	public static boolean hasProbeInBauble(EntityPlayer player) {
		if (TheOneProbe.baubles) {
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			int slots = baubles.getSlots();
			for(int i = 0; i < slots; i++) {
				if(canWorkAsProbe(baubles.getStackInSlot(i), player, PROBETAG)) {
					return true;
				}
			}
		}
		return false;
	}

}
