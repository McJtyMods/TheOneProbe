package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.compat.BaubleTools;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;

public class ModItems {
    public static CreativeProbe creativeProbe;
    public static Probe probe;
    public static Item diamondHelmetProbe;
    public static Item goldHelmetProbe;
    public static Item ironHelmetProbe;
    public static Item netheriteHelmetProbe;
    public static Item probeGoggles;
    public static ProbeNote probeNote;

    public static String PROBETAG = "theoneprobe";
    public static String PROBETAG_HAND = "theoneprobe_hand";

    public static void init() {
        probe = new Probe();
        creativeProbe = new CreativeProbe();

        TopArmorMaterial materialDiamondHelmet = new TopArmorMaterial("diamond_helmet_probe", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, null);
        TopArmorMaterial materialGoldHelmet = new TopArmorMaterial("gold_helmet_probe", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F, 0.0F, null);
        TopArmorMaterial materialIronHelmet = new TopArmorMaterial("iron_helmet_probe", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, null);
        TopArmorMaterial materialNetheriteHelmet = new TopArmorMaterial("netherite_helmet_probe", 37, new int[]{3, 6, 8, 3}, 15, SoundEvents.field_232681_Q_, 3.0F, 0.1F, null);

        diamondHelmetProbe = makeHelmet(materialDiamondHelmet, 3, "diamond_helmet_probe");
        goldHelmetProbe = makeHelmet(materialGoldHelmet, 4, "gold_helmet_probe");
        ironHelmetProbe = makeHelmet(materialIronHelmet, 2, "iron_helmet_probe");
        netheriteHelmetProbe = makeHelmet(materialNetheriteHelmet, 5, "netherite_helmet_probe");

        probeNote = new ProbeNote();

        if (TheOneProbe.baubles) {
            probeGoggles = BaubleTools.initProbeGoggle();
        }
    }

    private static Item makeHelmet(TopArmorMaterial material, int renderIndex, String name) {
        Item item = new ArmorItem(material, EquipmentSlotType.HEAD, new Item.Properties()
            .group(TheOneProbe.tabProbe)) {

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
        item.setRegistryName(name);
        return item;
    }

    public static boolean isProbeInHand(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getItem() == probe || stack.getItem() == creativeProbe) {
            return true;
        }
        if (stack.getTag() == null) {
            return false;
        }
        return stack.getTag().contains(PROBETAG_HAND);
    }

    private static boolean isProbeHelmet(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getTag() == null) {
            return false;
        }
        return stack.getTag().contains(PROBETAG);
    }

    public static boolean hasAProbeSomewhere(PlayerEntity player) {
        return hasProbeInHand(player, Hand.MAIN_HAND) || hasProbeInHand(player, Hand.OFF_HAND) || hasProbeInHelmet(player)
                || hasProbeInBauble(player);
    }

    private static boolean hasProbeInHand(PlayerEntity player, Hand hand) {
        ItemStack item = player.getHeldItem(hand);
        return isProbeInHand(item);
    }

    private static boolean hasProbeInHelmet(PlayerEntity player) {
        ItemStack helmet = player.inventory.getStackInSlot(36+3);
//        ItemStack helmet = player.inventory.armorInventory.get(3);
        return isProbeHelmet(helmet);
    }

    private static boolean hasProbeInBauble(PlayerEntity player) {
        if (TheOneProbe.baubles) {
            return BaubleTools.hasProbeGoggle(player);
        } else {
            return false;
        }
    }
}
