package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DebugProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.debug";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, Entity entity, IProbeHitEntityData data) {
        if (mode == ProbeMode.DEBUG && Config.showDebugInfo.get()) {
            IProbeInfo vertical = null;
            if (entity instanceof Mob mob) {
                vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));

                int totalArmorValue = mob.getArmorValue();
                int age = mob.getNoActionTime();
                float absorptionAmount = mob.getAbsorptionAmount();
                float aiMoveSpeed = mob.getSpeed();
                int revengeTimer = mob.getLastHurtByMobTimestamp();
                vertical
                        .text(CompoundText.createLabelInfo("Tot armor: ", totalArmorValue))
                        .text(CompoundText.createLabelInfo("Age: ", age))
                        .text(CompoundText.createLabelInfo("Absorption: ", absorptionAmount))
                        .text(CompoundText.createLabelInfo("AI Move Speed: ", aiMoveSpeed))
                        .text(CompoundText.createLabelInfo("Revenge Timer: ", revengeTimer));

                if (entity instanceof AgeableMob ageable) {
                    vertical
                            .text(CompoundText.createLabelInfo("Growing Age: ", ageable.getAge()));
                }
            }
        }
    }
}
