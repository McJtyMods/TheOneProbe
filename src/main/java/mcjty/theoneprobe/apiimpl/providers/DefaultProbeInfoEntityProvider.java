package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.ConfigSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.UUID;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;
import static mcjty.theoneprobe.api.TextStyleClass.*;

public class DefaultProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.default";
    }

    private static DecimalFormat dfCommas = new DecimalFormat("##.#");

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        IProbeConfig config = ConfigSetup.getRealConfig();

        boolean handled = false;
        for (IEntityDisplayOverride override : TheOneProbe.theOneProbeImp.getEntityOverrides()) {
            if (override.overrideStandardInfo(mode, probeInfo, player, world, entity, data)) {
                handled = true;
                break;
            }
        }
        if (!handled) {
            showStandardInfo(mode, probeInfo, entity, config);
        }

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;

            if (Tools.show(mode, config.getShowMobHealth())) {
                int health = (int) livingBase.getHealth();
                int maxHealth = (int) livingBase.getMaxHealth();
                int armor = livingBase.getTotalArmorValue();

                probeInfo.progress(health, maxHealth, probeInfo.defaultProgressStyle().lifeBar(true).showText(false).width(150).height(10));

                if (mode == ProbeMode.EXTENDED) {
                    probeInfo.text(LABEL + "Health: " + INFOIMP + health + " / " + maxHealth);
                }

                if (armor > 0) {
                    probeInfo.progress(armor, armor, probeInfo.defaultProgressStyle().armorBar(true).showText(false).width(80).height(10));
                }
            }

            if (Tools.show(mode, config.getShowMobGrowth()) && entity instanceof EntityAgeable) {
               int age = ((EntityAgeable) entity).getGrowingAge();
               if (age < 0) {
                   probeInfo.text(LABEL + "Growing time: " + ((age * -1) / 20) + "s");
               }
            }

            if (Tools.show(mode, config.getShowMobPotionEffects())) {
                Collection<PotionEffect> effects = livingBase.getActivePotionEffects();
                if (!effects.isEmpty()) {
                    IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xffffffff));
                    float durationFactor = 1.0f;
                    for (PotionEffect effect : effects) {
                        String s1 = STARTLOC + effect.getEffectName() + ENDLOC;
                        Potion potion = effect.getPotion();
                        if (effect.getAmplifier() > 0) {
                            s1 = s1 + " " + STARTLOC + "potion.potency." + effect.getAmplifier() + ENDLOC;
                        }

                        if (effect.getDuration() > 20) {
                            s1 = s1 + " (" + getPotionDurationString(effect, durationFactor) + ")";
                        }

                        if (potion.isBadEffect()) {
                            vertical.text(ERROR + s1);
                        } else {
                            vertical.text(OK + s1);
                        }
                    }
                }
            }
        } else if (entity instanceof EntityItemFrame) {
            EntityItemFrame itemFrame = (EntityItemFrame)entity;
            ItemStack stack = itemFrame.getDisplayedItem();
            if(!stack.isEmpty()) {
                probeInfo.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .item(stack, new ItemStyle().width(16).height(16))
                        .text(INFO + stack.getDisplayName());
                if (mode == ProbeMode.EXTENDED) {
                    probeInfo.text(LABEL + "Rotation: " + INFO + itemFrame.getRotation());
                }
            } else {
                probeInfo.text(LABEL + "Empty");
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
                    probeInfo.text(WARNING + "Unknown owner");
                } else {
                    probeInfo.text(LABEL + "Owned by: " + INFO + username);
                }
            } else if (entity instanceof EntityTameable) {
                probeInfo.text(LABEL + "Tameable");
            }
        }

        if (Tools.show(mode, config.getHorseStatSetting())) {
            if (entity instanceof EntityHorse) {
                double jumpStrength = ((EntityHorse) entity).getHorseJumpStrength();
                double jumpHeight = -0.1817584952 * jumpStrength * jumpStrength * jumpStrength + 3.689713992 * jumpStrength * jumpStrength + 2.128599134 * jumpStrength - 0.343930367;
                probeInfo.text(LABEL + "Jump height: " + INFO + dfCommas.format(jumpHeight));
                IAttributeInstance iattributeinstance = ((EntityHorse) entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                probeInfo.text(LABEL + "Speed: " + INFO + dfCommas.format(iattributeinstance.getAttributeValue()));
            }
        }

        if (entity instanceof EntityWolf && ConfigSetup.showCollarColor) {
            if (((EntityWolf) entity).isTamed()) {
                EnumDyeColor collarColor = ((EntityWolf) entity).getCollarColor();
                probeInfo.text(LABEL + "Collar: " + INFO + collarColor.getName());
            }
        }
    }

    public static String getPotionDurationString(PotionEffect effect, float factor) {
        if (effect.getDuration() == 32767) {
            return "**:**";
        } else {
            int i = MathHelper.floor(effect.getDuration() * factor);
            return ticksToElapsedTime(i);
        }
    }

    public static String ticksToElapsedTime(int ticks) {
        int i = ticks / 20;
        int j = i / 60;
        i = i % 60;
        return i < 10 ? j + ":0" + i : j + ":" + i;
    }


    public static void showStandardInfo(ProbeMode mode, IProbeInfo probeInfo, Entity entity, IProbeConfig config) {
        String modid = Tools.getModName(entity);

        if (Tools.show(mode, config.getShowModName())) {
            probeInfo.horizontal()
                    .entity(entity)
                    .vertical()
                        .text(NAME + entity.getDisplayName().getFormattedText())
                        .text(MODNAME + modid);
        } else {
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .entity(entity)
                    .text(NAME + entity.getDisplayName().getFormattedText());
        }
    }
}
