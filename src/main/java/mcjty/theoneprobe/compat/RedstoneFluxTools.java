package mcjty.theoneprobe.compat;

import net.minecraft.block.entity.BlockEntity;

public class RedstoneFluxTools {

    // @todo fabric
    public static int getEnergy(BlockEntity te) {
//        IEnergyHandler handler = (IEnergyHandler) te;
//        return handler.getEnergyStored(Direction.DOWN);
        return 0;
    }

    public static int getMaxEnergy(BlockEntity te) {
//        IEnergyHandler handler = (IEnergyHandler) te;
//        return handler.getMaxEnergyStored(Direction.DOWN);
        return 0;
    }

    public static boolean isEnergyHandler(BlockEntity te) {
//        return te instanceof IEnergyHandler;
        return false;
    }
}
