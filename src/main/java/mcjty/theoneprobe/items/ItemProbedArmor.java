package mcjty.theoneprobe.items;

import mcjty.lib.compat.CompatItemArmor;
import mcjty.theoneprobe.api.IProbeItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public abstract class ItemProbedArmor extends CompatItemArmor implements IProbeItem {

	public ItemProbedArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}
}
