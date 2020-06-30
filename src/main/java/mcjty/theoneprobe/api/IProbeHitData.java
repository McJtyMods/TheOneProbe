package mcjty.theoneprobe.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

/**
 * Access information about where the probe hit the block
 */
public interface IProbeHitData {

    BlockPos getPos();

    Vector3d getHitVec();

    Direction getSideHit();

    /**
     * Access the client-side result of getPickBlock() for the given block. That way
     * you don't have to call this server side because that can sometimes be
     * problematic
     * @return the picked block or ItemStack.EMPTY
     */
    @Nonnull
    ItemStack getPickBlock();
}
