package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

public class TopArmorMaterial implements IArmorMaterial {

    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11}; // Durability multipliers for Boots, Leggings, Chestplates, and Helmets respectively.
	private final String name;
    private final int durability;
    private final int[] damageReduction;
    private final int enchantability;
    private final float toughness;
    private final float knockbackResistance;
    private final SoundEvent soundEvent;
    private final Ingredient repairMaterial;


    public TopArmorMaterial(String name, int durability, int[] damageReduction, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Ingredient repairMaterial) {
        this.name = name;
        this.durability = durability;
        this.damageReduction = damageReduction;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getDurability(EquipmentSlotType entityEquipmentSlot) {
        return MAX_DAMAGE_ARRAY[entityEquipmentSlot.getIndex()] * durability;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType entityEquipmentSlot) {
        return damageReduction[entityEquipmentSlot.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    @Override
    public Ingredient getRepairMaterial() {
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
    public float func_230304_f_() {
        return this.knockbackResistance;
    }
}
