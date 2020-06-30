package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeHitData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class ProbeHitData implements IProbeHitData {

    private final BlockPos pos;
    private final Vector3d hitVec;
    private final Direction side;
    private final ItemStack pickBlock;

    public ProbeHitData(BlockPos pos, Vector3d hitVec, Direction side, @Nonnull ItemStack pickBlock) {
        this.pos = pos;
        this.hitVec = hitVec;
        this.side = side;
        this.pickBlock = pickBlock;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public Vector3d getHitVec() {
        return hitVec;
    }

    @Override
    public Direction getSideHit() {
        return side;
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock() {
        return pickBlock;
    }
}
