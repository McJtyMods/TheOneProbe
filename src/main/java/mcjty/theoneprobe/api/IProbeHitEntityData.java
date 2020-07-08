package mcjty.theoneprobe.api;

import net.minecraft.util.math.vector.Vector3d;

/**
 * Access information about where the probe hit the entity
 */
public interface IProbeHitEntityData {

    Vector3d getHitVec();
}
