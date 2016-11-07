package mcjty.theoneprobe.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BaubleTools {

    public static boolean hasProbeGoggle(EntityPlayer player) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        if (handler == null) {
            return false;
        }
        ItemStack stackInSlot = handler.getStackInSlot(4);
        if (stackInSlot != null && stackInSlot.getItem() == ModItems.probeGoggles) {
            return true;
        }
        return false;
    }
}
