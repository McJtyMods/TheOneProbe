package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;

public class DebugProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.debug";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, Entity entity, IProbeHitEntityData data) {
        if (mode == ProbeMode.DEBUG && Config.showDebugInfo) {
            IProbeInfo vertical = null;
            if (entity instanceof LivingEntity) {
                vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));

                LivingEntity entityLivingBase = (LivingEntity) entity;
                int totalArmorValue = entityLivingBase.method_6096();   // @todo fabric: getTotalArmorValue
                int age = entityLivingBase.method_6131();   // @todo fabric: getIdleTime();
                float absorptionAmount = entityLivingBase.getAbsorptionAmount();
                float aiMoveSpeed = entityLivingBase.method_6029(); // @todo fabric: entityLivingBase.getAIMoveSpeed();
                int revengeTimer = entityLivingBase.getLastAttackedTime(); // @todo fabric: entityLivingBase.getRevengeTimer();
                vertical
                        .text(LABEL + "Tot armor: " + INFO + totalArmorValue)
                        .text(LABEL + "Age: " + INFO + age)
                        .text(LABEL + "Absorption: " + INFO + absorptionAmount)
                        .text(LABEL + "AI Move Speed: " + INFO + aiMoveSpeed)
                        .text(LABEL + "Revenge Timer: " + INFO + revengeTimer);
            }
            if (entity instanceof PassiveEntity) {
                if (vertical == null) {
                    vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));
                }

                PassiveEntity entityAgeable = (PassiveEntity) entity;
                int growingAge = entityAgeable.getBreedingAge();
                vertical
                        .text(LABEL + "Growing Age: " + INFO + growingAge);
            }
        }
    }
}
