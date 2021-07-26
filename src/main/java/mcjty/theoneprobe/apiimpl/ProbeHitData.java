package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeHitData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public record ProbeHitData(BlockPos pos, Vec3 hitVec,
                           Direction side,
                           ItemStack pickBlock) implements IProbeHitData {

    public ProbeHitData(BlockPos pos, Vec3 hitVec, Direction side, @Nonnull ItemStack pickBlock) {
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
    public Vec3 getHitVec() {
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
