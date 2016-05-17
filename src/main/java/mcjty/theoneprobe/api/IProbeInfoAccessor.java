package mcjty.theoneprobe.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * You can implement this in your block implementation if you want to support
 * the probe. An alternative is to make an IProveInfoProvider.
 */
public interface IProbeInfoAccessor {

    /**
     * Add information for the probe info for the given block. This is always
     * called server side.
     */
    void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, World world, IBlockState blockState, BlockPos pos);
}
