package mcjty.theoneprobe.varia;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class Tools {

    public static int getSlotFor(ItemStack stack, EntityPlayerMP player, int startfrom) {
        for (int i = startfrom; i < player.inventory.mainInventory.length; ++i) {
            if (player.inventory.mainInventory[i] != null && stackEqualExact(stack, player.inventory.mainInventory[i])) {
                return i;
            }
        }
        return -1;
    }

    private static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
}
