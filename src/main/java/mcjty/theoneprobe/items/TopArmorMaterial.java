package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;

public class TopArmorMaterial implements ArmorMaterial {

    private final String name;
    private final int durability;
    private final int[] damageReduction;
    private final int enchantability;
    private final float toughness;
    private final SoundEvent soundEvent;
    private final Ingredient repairMaterial;


    public TopArmorMaterial(String name, int durability, int[] damageReduction, int enchantability, SoundEvent soundEvent, float toughness, Ingredient repairMaterial) {
        this.name = name;
        this.durability = durability;
        this.damageReduction = damageReduction;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type entityEquipmentSlot) {
        return durability;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type entityEquipmentSlot) {
        return damageReduction[entityEquipmentSlot.ordinal()];
    }


    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial;
    }

    @Override
    public String getName() {
        return TheOneProbe.MODID + ":" + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
