package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.compat.BaubleTools;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static mcjty.theoneprobe.TheOneProbe.HASPROBE_TAG;

public class ModItems {
    public static CreativeProbe CREATIVE_PROBE;
    public static Probe PROBE;
    public static Item DIAMOND_HELMET_PROBE;
    public static Item GOLD_HELMET_PROBE;
    public static Item IRON_HELMET_PROBE;
    public static Item PROBE_GOGGLES;
    public static ProbeNote PROBE_NOTE;

    public static void init() {
        PROBE = new Probe();
        CREATIVE_PROBE = new CreativeProbe();

        DIAMOND_HELMET_PROBE = makeHelmet(TheOneProbe.MATERIAL_DIAMOND_HELMET);
        GOLD_HELMET_PROBE = makeHelmet(TheOneProbe.MATERIAL_GOLD_HELMET);
        IRON_HELMET_PROBE = makeHelmet(TheOneProbe.MATERIAL_IRON_HELMET);

        PROBE_NOTE = new ProbeNote();

        if (TheOneProbe.baubles) {
            PROBE_GOGGLES = BaubleTools.initProbeGoggle();
        }
    }

    private static Item makeHelmet(Holder<ArmorMaterial> material) {
        Item item = new ArmorItem(material, ArmorItem.Type.HELMET, new Item.Properties()) {

//            @Override
//            public boolean getHasSubtypes() {
//                return true;
//            }
//
//            @Override
//            public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
//                if (this.isInCreativeTab(tab)) {
//                    ItemStack stack = new ItemStack(this);
//                    CompoundNBT tag = new CompoundNBT();
//                    tag.setInteger(PROBETAG, 1);
//                    stack.setTagCompound(tag);
//                    subItems.add(stack);
//                }
//            }
        };
        return item;
    }

    public static boolean isProbeInHand(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getItem() == PROBE || stack.getItem() == CREATIVE_PROBE) {
            return true;
        }
        return stack.is(HASPROBE_TAG);
    }

    private static boolean isProbeHelmet(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return stack.is(HASPROBE_TAG);
    }

    public static boolean hasAProbeSomewhere(Player player) {
        return hasProbeInHand(player, InteractionHand.MAIN_HAND) || hasProbeInHand(player, InteractionHand.OFF_HAND) || hasProbeInHelmet(player)
                || hasProbeInBauble(player);
    }

    private static boolean hasProbeInHand(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        return isProbeInHand(item);
    }

    private static boolean hasProbeInHelmet(Player player) {
        ItemStack helmet = player.getInventory().getItem(36+3);
//        ItemStack helmet = player.inventory.armorInventory.get(3);
        return isProbeHelmet(helmet);
    }

    private static boolean hasProbeInBauble(Player player) {
        if (TheOneProbe.baubles) {
            return BaubleTools.hasProbeGoggle(player);
        } else {
            return false;
        }
    }
}
