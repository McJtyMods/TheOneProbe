package mcjty.theoneprobe.apiimpl;

import com.sun.istack.internal.Nullable;
import mcjty.theoneprobe.api.IProbeHitData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ProbeHitData implements IProbeHitData {

    private final BlockPos pos;
    private final Vec3d hitVec;
    private final Direction side;
    private final ItemStack pickBlock;

    public ProbeHitData(BlockPos pos, Vec3d hitVec, Direction side, ItemStack pickBlock) {
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
    public Vec3d getHitVec() {
        return hitVec;
    }

    @Override
    public Direction getSideHit() {
        return side;
    }

    @Nullable
    @Override
    public ItemStack getPickBlock() {
        return pickBlock;
    }
}
