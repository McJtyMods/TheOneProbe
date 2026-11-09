package mcjty.theoneprobe.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A side-safe predicate that hides the entire probe overlay for a block in the world.
 */
@FunctionalInterface
public interface IBlockBlacklist {

    /**
     * Return true to hide the block from the viewing player. Called on the client and server;
     * implementations must not modify game state or access client-only classes.
     */
    boolean test(Player player, Level level, BlockPos pos, BlockState state);
}
