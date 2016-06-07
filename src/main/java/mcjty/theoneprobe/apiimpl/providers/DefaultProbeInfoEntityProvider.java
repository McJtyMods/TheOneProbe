package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.Collection;

public class DefaultProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.default";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        IProbeConfig config = Config.getRealConfig();

        String modid = Tools.getModName(entity);

        if (Tools.show(mode, config.getShowModName())) {
            probeInfo.horizontal()
                    .entity(entity)
                    .vertical()
                        .text(TextFormatting.WHITE + entity.getDisplayName().getFormattedText())
                        .text(TextFormatting.BLUE + modid);
        } else {
            probeInfo.horizontal()
                    .entity(entity)
                    .text(TextFormatting.WHITE + entity.getDisplayName().getFormattedText());
        }

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;
            if (Tools.show(mode, config.getShowMobHealth())) {
                int health = (int) livingBase.getHealth();
                int maxHealth = (int) livingBase.getMaxHealth();
                probeInfo.progress(health, maxHealth, probeInfo.defaultProgressStyle().lifeBar(true).showText(false).width(150).height(10));
                if (mode == ProbeMode.EXTENDED) {
                    probeInfo.text(TextFormatting.YELLOW + "Health: " + health + " / " + maxHealth);
                }
            }

            if (Tools.show(mode, config.getShowMobPotionEffects())) {
                Collection<PotionEffect> effects = livingBase.getActivePotionEffects();
                if (!effects.isEmpty()) {
                    IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xffffffff));
                    float durationFactor = 1.0f;
                    for (PotionEffect effect : effects) {
                        String s1 = I18n.translateToLocal(effect.getEffectName()).trim();
                        Potion potion = effect.getPotion();
                        if (effect.getAmplifier() > 0) {
                            s1 = s1 + " " + I18n.translateToLocal("potion.potency." + effect.getAmplifier()).trim();
                        }

                        if (effect.getDuration() > 20) {
                            s1 = s1 + " (" + Potion.getPotionDurationString(effect, durationFactor) + ")";
                        }

                        if (potion.isBadEffect()) {
                            vertical.text(TextFormatting.RED + s1);
                        } else {
                            vertical.text(TextFormatting.GREEN + s1);
                        }
                    }
                }
            }
        }
    }
}
