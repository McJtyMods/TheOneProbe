package mcjty.theoneprobe.items;

import mcjty.theoneprobe.api.IProbeItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public abstract class ItemProbedArmor extends ItemArmor implements IProbeItem {

	public ItemProbedArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}
}
