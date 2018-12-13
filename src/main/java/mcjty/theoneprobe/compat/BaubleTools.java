package mcjty.theoneprobe.compat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public class BaubleTools {

    public static boolean hasProbeGoggle(PlayerEntity player) {
        // @todo fabric
//        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
//        if (handler == null) {
//            return false;
//        }
//        ItemStack stackInSlot = handler.getStackInSlot(4);
//        if (!stackInSlot.isEmpty() && stackInSlot.getItem() == ModItems.probeGoggles) {
//            return true;
//        }
        return false;
    }

    public static Item initProbeGoggle() {
        return new ProbeGoggles();
    }

//    @SideOnly(Side.CLIENT)
//    public static void initProbeModel(Item probeGoggle) {
//        ((ProbeGoggles) probeGoggle).initModel();
//    }

}
