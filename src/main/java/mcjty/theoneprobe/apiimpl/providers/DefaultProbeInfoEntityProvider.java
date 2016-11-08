package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.UUID;

public class DefaultProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.default";
    }

    private static DecimalFormat dfCommas = new DecimalFormat("##.#");

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        IProbeConfig config = Config.getRealConfig();

        showStandardInfo(mode, probeInfo, entity, config);

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

        if (Tools.show(mode, config.getAnimalOwnerSetting())) {
            UUID ownerId = null;
            if (entity instanceof IEntityOwnable) {
                ownerId = ((IEntityOwnable) entity).getOwnerId();
            } else if (entity instanceof EntityHorse) {
                ownerId = ((EntityHorse) entity).getOwnerUniqueId();
            }

            if (ownerId != null) {
                String username = UsernameCache.getLastKnownUsername(ownerId);
                if (username == null) {
                    probeInfo.text(TextFormatting.YELLOW + "Unknown owner");
                } else {
                    probeInfo.text(TextFormatting.GREEN + "Owned by: " + username);
                }
            }
        }

        if (Tools.show(mode, config.getHorseStatSetting())) {
            if (entity instanceof EntityHorse) {
                double jumpStrength = ((EntityHorse) entity).getHorseJumpStrength();
                double jumpHeight = -0.1817584952 * jumpStrength * jumpStrength * jumpStrength + 3.689713992 * jumpStrength * jumpStrength + 2.128599134 * jumpStrength - 0.343930367;
                probeInfo.text(TextFormatting.GREEN + "Jump height: " + dfCommas.format(jumpHeight));
                IAttributeInstance iattributeinstance = ((EntityHorse) entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                probeInfo.text(TextFormatting.GREEN + "Speed: " + dfCommas.format(iattributeinstance.getAttributeValue()));
            }
        }

        if (entity instanceof EntityWolf && Config.showCollarColor) {
            EnumDyeColor collarColor = ((EntityWolf) entity).getCollarColor();
            probeInfo.text("Collar: " + collarColor.getName());
        }
    }

    public static void showStandardInfo(ProbeMode mode, IProbeInfo probeInfo, Entity entity, IProbeConfig config) {
        String modid = Tools.getModName(entity);

        if (Tools.show(mode, config.getShowModName())) {
            probeInfo.horizontal()
                    .entity(entity)
                    .vertical()
                        .text(TextFormatting.WHITE + entity.getDisplayName().getFormattedText())
                        .text(TextFormatting.BLUE + modid);
        } else {
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .entity(entity)
                    .text(TextFormatting.WHITE + entity.getDisplayName().getFormattedText());
        }
    }
}
