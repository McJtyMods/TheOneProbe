package mcjty.theoneprobe.api;

import java.util.EnumMap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Implement this interface if you want a custom display for your own blocks instead of the standard
 * display. This can be useful if you (for example) have a multiblock and want to show a picture of the
 * entire multiblock instead of the itemstack that getPickBlock() would return.
 */
public interface IBlockDisplayOverride {

    /**
     * This function returns true if you handled the probe info yourselves and TOP doesn't have to add its
     * own info.
     */
    boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data);
    
    default boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data, EnumMap<DisplayFlag, Runnable> overrides) {
    	return overrideStandardInfo(mode, probeInfo, player, world, blockState, data);
    }
    
    public static enum DisplayFlag {
    	GROWTH_INFO,
    	HARVEST_INFO,
    	REDSTONE_INFO,
    	LEVER_INFO,
    	CHEST_INFO,
    	RF_INFO,
    	TANK_INFO,
    	BREW_INFO,
    	MOB_INFO,
    	NOTE_BLOCK_INFO,
    	SKULL_INFO;
    }
}
