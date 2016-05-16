package mcjty.theoneprobe.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * You can implement this in your block implementation if you want to support
 * the probe. An alternative is to make an IProveInfoProvider.
 */
public interface IProbeInfoAccessor {

    /**
     * Return true if this block needs information server side. Return false if
     * everything this block wants to display can be calculated on the client.
     */
    boolean needsServerInfo(World world, IBlockState blockState, BlockPos pos);

    /**
     * Add information for the probe info for the given block
     */
    void addProbeInfo(IProbeInfo probeInfo, World world, IBlockState blockState, BlockPos pos, EntityPlayer player);
}
