package mcjty.theoneprobe.compat;

import cofh.redstoneflux.api.IEnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class RedstoneFluxTools {

    public static int getEnergy(TileEntity te) {
        IEnergyHandler handler = (IEnergyHandler) te;
        return handler.getEnergyStored(EnumFacing.DOWN);
    }

    public static int getMaxEnergy(TileEntity te) {
        IEnergyHandler handler = (IEnergyHandler) te;
        return handler.getMaxEnergyStored(EnumFacing.DOWN);
    }

    public static boolean isEnergyHandler(TileEntity te) {
        return te instanceof IEnergyHandler;
    }
}
