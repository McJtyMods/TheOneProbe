package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;

public class DebugProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.debug";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (mode == ProbeMode.DEBUG && ConfigSetup.showDebugInfo) {
            IProbeInfo vertical = null;
            if (entity instanceof EntityLivingBase) {
                vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));

                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                int totalArmorValue = entityLivingBase.getTotalArmorValue();
                int age = entityLivingBase.getIdleTime();
                float absorptionAmount = entityLivingBase.getAbsorptionAmount();
                float aiMoveSpeed = entityLivingBase.getAIMoveSpeed();
                int revengeTimer = entityLivingBase.getRevengeTimer();
                vertical
                        .text(LABEL + "Tot armor: " + INFO + totalArmorValue)
                        .text(LABEL + "Age: " + INFO + age)
                        .text(LABEL + "Absorption: " + INFO + absorptionAmount)
                        .text(LABEL + "AI Move Speed: " + INFO + aiMoveSpeed)
                        .text(LABEL + "Revenge Timer: " + INFO + revengeTimer);
            }
            if (entity instanceof EntityAgeable) {
                if (vertical == null) {
                    vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2));
                }

                EntityAgeable entityAgeable = (EntityAgeable) entity;
                int growingAge = entityAgeable.getGrowingAge();
                vertical
                        .text(LABEL + "Growing Age: " + INFO + growingAge);
            }
        }
    }
}
