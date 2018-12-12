package mcjty.theoneprobe.compat;

import cofh.redstoneflux.api.IEnergyHandler;
import net.minecraft.BlockEntity.BlockEntity;
import net.minecraft.util.EnumFacing;

public class RedstoneFluxTools {

    public static int getEnergy(BlockEntity te) {
        IEnergyHandler handler = (IEnergyHandler) te;
        return handler.getEnergyStored(EnumFacing.DOWN);
    }

    public static int getMaxEnergy(BlockEntity te) {
        IEnergyHandler handler = (IEnergyHandler) te;
        return handler.getMaxEnergyStored(EnumFacing.DOWN);
    }

    public static boolean isEnergyHandler(BlockEntity te) {
        return te instanceof IEnergyHandler;
    }
}
