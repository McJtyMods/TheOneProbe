package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.world.entity.AgableMob;
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
            if (entity instanceof Mob) {
                vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));

                Mob entityLivingBase = (Mob) entity;
                int totalArmorValue = entityLivingBase.getArmorValue();
                int age = entityLivingBase.getNoActionTime();
                float absorptionAmount = entityLivingBase.getAbsorptionAmount();
                float aiMoveSpeed = entityLivingBase.getSpeed();
                int revengeTimer = entityLivingBase.getLastHurtByMobTimestamp();
                vertical
                        .text(CompoundText.createLabelInfo("Tot armor: ", totalArmorValue))
                        .text(CompoundText.createLabelInfo("Age: ", age))
                        .text(CompoundText.createLabelInfo("Absorption: ", absorptionAmount))
                        .text(CompoundText.createLabelInfo("AI Move Speed: ", aiMoveSpeed))
                        .text(CompoundText.createLabelInfo("Revenge Timer: ", revengeTimer));
            }
            if (entity instanceof AgableMob) {
                if (vertical == null) {
                    vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));
                }

                AgableMob entityAgeable = (AgableMob) entity;
                int growingAge = entityAgeable.getAge();
                vertical
                        .text(CompoundText.createLabelInfo("Growing Age: ", growingAge));
            }
        }
    }
}
