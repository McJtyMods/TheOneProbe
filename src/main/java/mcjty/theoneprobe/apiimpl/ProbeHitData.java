package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeHitData;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ProbeHitData implements IProbeHitData {

    private final BlockPos pos;
    private final Vec3d hitVec;
    private final EnumFacing side;


    public ProbeHitData(BlockPos pos, Vec3d hitVec, EnumFacing side) {
        this.pos = pos;
        this.hitVec = hitVec;
        this.side = side;
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
    public EnumFacing getSideHit() {
        return side;
    }
}
