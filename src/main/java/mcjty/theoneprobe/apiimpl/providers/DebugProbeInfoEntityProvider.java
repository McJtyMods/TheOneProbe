package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
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
        if (mode == ProbeMode.DEBUG && Config.showDebugInfo.get()) {
            IProbeInfo vertical = null;
            if (entity instanceof MobEntity) {
                vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));

                MobEntity entityLivingBase = (MobEntity) entity;
                int totalArmorValue = entityLivingBase.getTotalArmorValue();
                int age = entityLivingBase.getIdleTime();
                float absorptionAmount = entityLivingBase.getAbsorptionAmount();
                float aiMoveSpeed = entityLivingBase.getAIMoveSpeed();
                int revengeTimer = entityLivingBase.getRevengeTimer();
                vertical
                        .text(CompoundText.create().style(LABEL).text("Tot armor: ").style(INFO).text(String.valueOf(totalArmorValue)).get())
                        .text(CompoundText.create().style(LABEL).text("Age: ").style(INFO).text(String.valueOf(age)).get())
                        .text(CompoundText.create().style(LABEL).text("Absorption: ").style(INFO).text(String.valueOf(absorptionAmount)).get())
                        .text(CompoundText.create().style(LABEL).text("AI Move Speed: ").style(INFO).text(String.valueOf(aiMoveSpeed)).get())
                        .text(CompoundText.create().style(LABEL).text("Revenge Timer: ").style(INFO).text(String.valueOf(revengeTimer)).get());
            }
            if (entity instanceof AgeableEntity) {
                if (vertical == null) {
                    vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));
                }

                AgeableEntity entityAgeable = (AgeableEntity) entity;
                int growingAge = entityAgeable.getGrowingAge();
                vertical
                        .text(CompoundText.create().style(LABEL).text("Growing Age: ").style(INFO).text(String.valueOf(growingAge)).get());
            }
        }
    }
}
