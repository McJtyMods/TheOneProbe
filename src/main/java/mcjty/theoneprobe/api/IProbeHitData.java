package mcjty.theoneprobe.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Access information about where the probe hit the block
 */
public interface IProbeHitData {

    BlockPos getPos();

    Vec3d getHitVec();

    EnumFacing getSideHit();
}
