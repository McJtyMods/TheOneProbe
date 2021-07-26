package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.entity";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof IProbeInfoEntityAccessor) {
            IProbeInfoEntityAccessor accessor = (IProbeInfoEntityAccessor) entity;
            accessor.addProbeInfo(mode, probeInfo, player, world, entity, data);
        }
    }
}
