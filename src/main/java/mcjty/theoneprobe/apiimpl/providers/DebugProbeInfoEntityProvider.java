package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DebugProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.debug";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, Entity entity, IProbeHitEntityData data) {
        if (mode == ProbeMode.DEBUG && Config.showDebugInfo.get()) {
            IProbeInfo vertical = null;
            if (entity instanceof MobEntity) {
                vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));

                MobEntity entityLivingBase = (MobEntity) entity;
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
            if (entity instanceof AgeableEntity) {
                if (vertical == null) {
                    vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));
                }

                AgeableEntity entityAgeable = (AgeableEntity) entity;
                int growingAge = entityAgeable.getAge();
                vertical
                        .text(CompoundText.createLabelInfo("Growing Age: ", growingAge));
            }
        }
    }
}
