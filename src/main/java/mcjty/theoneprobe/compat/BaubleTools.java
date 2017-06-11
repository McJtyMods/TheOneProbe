package mcjty.theoneprobe.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaubleTools {

    public static boolean hasProbeGoggle(EntityPlayer player) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        if (handler == null) {
            return false;
        }
        ItemStack stackInSlot = handler.getStackInSlot(4);
        if (!stackInSlot.isEmpty() && stackInSlot.getItem() == ModItems.probeGoggles) {
            return true;
        }
        return false;
    }

    public static Item initProbeGoggle() {
        return new ProbeGoggles();
    }

    @SideOnly(Side.CLIENT)
    public static void initProbeModel(Item probeGoggle) {
        ((ProbeGoggles) probeGoggle).initModel();
    }

}
