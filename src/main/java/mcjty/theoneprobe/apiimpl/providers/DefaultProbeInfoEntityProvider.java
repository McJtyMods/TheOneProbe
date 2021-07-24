package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.UsernameCache;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.UUID;

import static mcjty.theoneprobe.api.TextStyleClass.*;

public class DefaultProbeInfoEntityProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":entity.default";
    }

    private static DecimalFormat dfCommas = new DecimalFormat("##.##");

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, Entity entity, IProbeHitEntityData data) {
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

        if (entity instanceof Mob) {
            Mob livingBase = (Mob) entity;

            if (Tools.show(mode, config.getShowMobHealth())) {
                int health = (int) livingBase.getHealth();
                int maxHealth = (int) livingBase.getMaxHealth();
                int armor = livingBase.getArmorValue();

                probeInfo.progress(health, maxHealth, probeInfo.defaultProgressStyle().lifeBar(true).showText(false).width(150).height(10));

                if (mode == ProbeMode.EXTENDED) {
                    probeInfo.text(CompoundText.createLabelInfo("Health: ",health + " / " + maxHealth));
                }

                if (armor > 0) {
                    probeInfo.progress(armor, armor, probeInfo.defaultProgressStyle().armorBar(true).showText(false).width(80).height(10));
                }
            }

            if (Tools.show(mode, config.getShowMobGrowth()) && entity instanceof AgableMob) {
               int age = ((AgableMob) entity).getAge();
               if (age < 0) {
                   probeInfo.text(CompoundText.createLabelInfo("Growing time: ",+ ((age * -1) / 20) + "s"));
               }
            }

            if (Tools.show(mode, config.getShowMobPotionEffects())) {
                Collection<MobEffectInstance> effects = livingBase.getActiveEffects();
                if (!effects.isEmpty()) {
                    IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xffffffff));
                    float durationFactor = 1.0f;
                    for (MobEffectInstance effect : effects) {
                        CompoundText text = CompoundText.create().info(effect.getDescriptionId());
                        MobEffect potion = effect.getEffect();
                        if (!potion.isBeneficial()) {
                            text.style(ERROR);
                        } else {
                            text.style(OK);
                        }
                        if (effect.getAmplifier() > 0) {
                            text.text(" ").info("potion.potency." + effect.getAmplifier());
                        }

                        if (effect.getDuration() > 20) {
                            text.text(" (" + getPotionDurationString(effect, durationFactor) + ")");
                        }

                        vertical.text(text);
                    }
                }
            }
        } else if (entity instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame)entity;
            ItemStack stack = itemFrame.getItem();
            if(!stack.isEmpty()) {
                probeInfo.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .item(stack, new ItemStyle().width(16).height(16))
                        .text(CompoundText.create().info(stack.getDescriptionId()));
                if (mode == ProbeMode.EXTENDED) {
                    probeInfo.text(CompoundText.createLabelInfo("Rotation: ", itemFrame.getRotation()));
                }
            } else {
                probeInfo.text(CompoundText.create().style(LABEL).text("Empty"));
            }
        }

        if (Tools.show(mode, config.getAnimalOwnerSetting())) {
            UUID ownerId = null;
            if (entity instanceof TamableAnimal) {
                ownerId = ((TamableAnimal) entity).getOwnerUUID();
            } else if (entity instanceof Horse) {
                ownerId = ((Horse) entity).getOwnerUUID();
            }

            if (ownerId != null) {
                String username = UsernameCache.getLastKnownUsername(ownerId);
                if (username == null) {
                    probeInfo.text(CompoundText.create().style(WARNING).text("Unknown owner"));
                } else {
                    probeInfo.text(CompoundText.createLabelInfo("Owned by: ", username));
                }
            } else if (entity instanceof TamableAnimal) {
                probeInfo.text(CompoundText.create().style(LABEL).text("Tameable"));
            }
        }

        if (Tools.show(mode, config.getHorseStatSetting())) {
            if (entity instanceof Horse) {
                double jumpStrength = ((Horse) entity).getCustomJump();
                double jumpHeight = -0.1817584952 * jumpStrength * jumpStrength * jumpStrength + 3.689713992 * jumpStrength * jumpStrength + 2.128599134 * jumpStrength - 0.343930367;
                probeInfo.text(CompoundText.createLabelInfo("Jump height: ", dfCommas.format(jumpHeight)));
                AttributeInstance iattributeinstance = ((Horse) entity).getAttribute(Attributes.MOVEMENT_SPEED);
                probeInfo.text(CompoundText.createLabelInfo("Speed: ", dfCommas.format(iattributeinstance.getValue())));
            }
        }

        if (entity instanceof Wolf && Config.showCollarColor.get()) {
            if (((Wolf) entity).isTame()) {
                DyeColor collarColor = ((Wolf) entity).getCollarColor();
                probeInfo.text(CompoundText.createLabelInfo("Collar: ", collarColor.getSerializedName()));
            }
        }
    }

    public static String getPotionDurationString(MobEffectInstance effect, float factor) {
        if (effect.getDuration() == 32767) {
            return "**:**";
        } else {
            int i = Mth.floor(effect.getDuration() * factor);
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
        String modName = Tools.getModName(entity.getType());

        if (Tools.show(mode, config.getShowModName())) {
            probeInfo.horizontal()
                    .entity(entity)
                    .vertical()
                    .text(CompoundText.create().name(entity.getName()))
                    .text(CompoundText.create().style(MODNAME).text(modName));
        } else {
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .entity(entity)
                    .text(CompoundText.create().name(entity.getName()));
        }
    }
}
