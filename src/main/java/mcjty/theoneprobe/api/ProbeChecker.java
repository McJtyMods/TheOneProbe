package mcjty.theoneprobe.api;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.google.common.collect.Sets;
import mcjty.lib.tools.ItemStackTools;
import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.Set;


public class ProbeChecker {

	public static String PROBETAG = "theoneprobe";
	public static String PROBETAG_HAND = "theoneprobe_hand";
	private static final Set<IProbeCheck> CHECKS = Sets.newHashSet();

	//Default checks
	static {
		addCheck(p -> hasProbeInHand(p, EnumHand.MAIN_HAND));
		addCheck(p -> hasProbeInHand(p, EnumHand.OFF_HAND));
		addCheck(ProbeChecker::hasProbeInHelmet);
		addCheck(ProbeChecker::hasProbeInBauble);
	}


	public static boolean canWorkAsProbe(ItemStack stack, EntityPlayer player, String nbtProbeTagName) {
		if (ItemStackTools.isEmpty(stack)) {
			return false;
		}
		if (stack.getItem() instanceof IProbeItem) {
			return ((IProbeItem) stack.getItem()).canWorkAsProbe(stack, player);
		}
		return stack.getTagCompound() != null && stack.getTagCompound().hasKey(nbtProbeTagName);
	}

	public static boolean hasAProbeSomewhere(EntityPlayer player) {
		return CHECKS.parallelStream().anyMatch(c -> c.hasProbe(player));
	}

	public static boolean hasProbeInHand(EntityPlayer player, EnumHand hand) {
		return canWorkAsProbe(player.getHeldItem(hand), player, PROBETAG_HAND);
	}

	public static boolean hasProbeInHelmet(EntityPlayer player) {
		return canWorkAsProbe(player.inventory.getStackInSlot(36+3), player, PROBETAG);
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

	public static void addCheck(IProbeCheck check) {
		CHECKS.add(check);
	}
}
