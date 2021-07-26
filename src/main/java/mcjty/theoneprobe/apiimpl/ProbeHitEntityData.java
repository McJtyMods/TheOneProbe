package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import net.minecraft.world.phys.Vec3;

public class ProbeHitEntityData implements IProbeHitEntityData {

    private final Vec3 hitVec;

    public ProbeHitEntityData(Vec3 hitVec) {
        this.hitVec = hitVec;
    }

    @Override
    public Vec3 getHitVec() {
        return hitVec;
    }
}
