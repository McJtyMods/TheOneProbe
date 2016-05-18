package mcjty.theoneprobe.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * You can implement IProbeInfoAccessor in your blocks or else you can use
 * this and register that to the ITheOneProbe. Note that TheOneProbe already
 * adds one default provider which gives basic block information.
 */
public interface IProbeInfoProvider {

    /**
     * Add information for the probe info for the given block. This is always called
     * server side.
     * The given probeInfo object represents a vertical layout. So adding elements to that
     * will cause them to be grouped vertically.
     */
    void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data);
}
