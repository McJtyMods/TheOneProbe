package mcjty.theoneprobe.compat;

import net.minecraft.tileentity.TileEntity;

public class TeslaTools {

    public static long getEnergy(TileEntity te) {
//        ITeslaHolder handler = te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
//        return handler.getStoredPower();
        return 0;
    }

    public static long getMaxEnergy(TileEntity te) {
//        ITeslaHolder handler = te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
//        return handler.getCapacity();
        return 0;
    }

    public static boolean isEnergyHandler(TileEntity te) {
//        return te != null && te.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        return false;
    }
}
