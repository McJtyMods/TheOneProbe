package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
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
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, Entity entity, IProbeHitEntityData data) {
        IProbeConfig config = Config.getRealConfig();

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

        if (entity instanceof MobEntity) {
            MobEntity livingBase = (MobEntity) entity;

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

            if (Tools.show(mode, config.getShowMobGrowth()) && entity instanceof AgeableEntity) {
               int age = ((AgeableEntity) entity).getGrowingAge();
               if (age < 0) {
                   probeInfo.text(LABEL + "Growing time: " + ((age * -1) / 20) + "s");
               }
            }

            if (Tools.show(mode, config.getShowMobPotionEffects())) {
                Collection<EffectInstance> effects = livingBase.getActivePotionEffects();
                if (!effects.isEmpty()) {
                    IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xffffffff));
                    float durationFactor = 1.0f;
                    for (EffectInstance effect : effects) {
                        String s1 = STARTLOC + effect.getEffectName() + ENDLOC;
                        Effect potion = effect.getPotion();
                        if (effect.getAmplifier() > 0) {
                            s1 = s1 + " " + STARTLOC + "potion.potency." + effect.getAmplifier() + ENDLOC;
                        }

                        if (effect.getDuration() > 20) {
                            s1 = s1 + " (" + getPotionDurationString(effect, durationFactor) + ")";
                        }

                        if (!potion.isBeneficial()) {
                            vertical.text(ERROR + s1);
                        } else {
                            vertical.text(OK + s1);
                        }
                    }
                }
            }
        } else if (entity instanceof ItemFrameEntity) {
            ItemFrameEntity itemFrame = (ItemFrameEntity)entity;
            ItemStack stack = itemFrame.getDisplayedItem();
            if(!stack.isEmpty()) {
                probeInfo.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .item(stack, new ItemStyle().width(16).height(16))
                        .text(INFO + stack.getDisplayName().getString());
                if (mode == ProbeMode.EXTENDED) {
                    probeInfo.text(LABEL + "Rotation: " + INFO + itemFrame.getRotation());
                }
            } else {
                probeInfo.text(LABEL + "Empty");
            }
        }

        if (Tools.show(mode, config.getAnimalOwnerSetting())) {
            UUID ownerId = null;
            if (entity instanceof TameableEntity) {
                ownerId = ((TameableEntity) entity).getOwnerId();
            } else if (entity instanceof HorseEntity) {
                ownerId = ((HorseEntity) entity).getOwnerUniqueId();
            }

            if (ownerId != null) {
                String username = UsernameCache.getLastKnownUsername(ownerId);
                if (username == null) {
                    probeInfo.text(WARNING + "Unknown owner");
                } else {
                    probeInfo.text(LABEL + "Owned by: " + INFO + username);
                }
            } else if (entity instanceof TameableEntity) {
                probeInfo.text(LABEL + "Tameable");
            }
        }

        if (Tools.show(mode, config.getHorseStatSetting())) {
            if (entity instanceof HorseEntity) {
                double jumpStrength = ((HorseEntity) entity).getHorseJumpStrength();
                double jumpHeight = -0.1817584952 * jumpStrength * jumpStrength * jumpStrength + 3.689713992 * jumpStrength * jumpStrength + 2.128599134 * jumpStrength - 0.343930367;
                probeInfo.text(LABEL + "Jump height: " + INFO + dfCommas.format(jumpHeight));
                ModifiableAttributeInstance iattributeinstance = ((HorseEntity) entity).getAttribute(Attributes.field_233821_d_);
                probeInfo.text(LABEL + "Speed: " + INFO + dfCommas.format(iattributeinstance.getValue()));
            }
        }

        if (entity instanceof WolfEntity && Config.showCollarColor.get()) {
            if (((WolfEntity) entity).isTamed()) {
                DyeColor collarColor = ((WolfEntity) entity).getCollarColor();
                probeInfo.text(LABEL + "Collar: " + INFO + collarColor.func_176610_l());
            }
        }
    }

    public static String getPotionDurationString(EffectInstance effect, float factor) {
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
                        .text(NAME + entity.getDisplayName().getString())
                        .text(MODNAME + modid);
        } else {
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .entity(entity)
                    .text(NAME + entity.getDisplayName().getString());
        }
    }
}
