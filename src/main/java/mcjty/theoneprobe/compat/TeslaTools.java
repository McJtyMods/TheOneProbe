package mcjty.theoneprobe.compat;

import net.minecraft.block.entity.BlockEntity;

public class TeslaTools {

    // @todo fabric

    public static long getEnergy(BlockEntity te) {
        return 0;
//        ITeslaHolder handler = te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
//        return handler.getStoredPower();
    }

    public static long getMaxEnergy(BlockEntity te) {
//        ITeslaHolder handler = te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
//        return handler.getCapacity();
        return 0;
    }

    public static boolean isEnergyHandler(BlockEntity te) {
//        return te != null && te.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        return false;
    }
}
