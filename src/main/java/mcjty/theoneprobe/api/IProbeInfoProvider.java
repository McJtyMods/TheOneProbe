package mcjty.theoneprobe.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * You can implement IProbeInfoAccessor in your blocks or else you can use
 * this and register that to the ITheOneProbe.
 */
public interface IProbeInfoProvider {

    /**
     * Add information for the probe info for the given block. This is always called
     * server side.
     */
    void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, World world, IBlockState blockState, BlockPos pos);
}
