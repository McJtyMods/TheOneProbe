package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.compat.BaubleTools;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

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
        PROBE = new Probe(properties("probe"));
        CREATIVE_PROBE = new CreativeProbe(properties("creativeprobe"));

        DIAMOND_HELMET_PROBE = makeHelmet("diamond_helmet_probe", ArmorMaterials.DIAMOND);
        GOLD_HELMET_PROBE = makeHelmet("gold_helmet_probe", ArmorMaterials.GOLD);
        IRON_HELMET_PROBE = makeHelmet("iron_helmet_probe", ArmorMaterials.IRON);

        PROBE_NOTE = new ProbeNote(properties("probenote"));

        if (TheOneProbe.baubles) {
            PROBE_GOGGLES = BaubleTools.initProbeGoggle();
        }
    }

    private static Item makeHelmet(String name, ArmorMaterial material) {
        return new Item(properties(name).humanoidArmor(material, ArmorType.HELMET));
    }

    private static Item.Properties properties(String name) {
        ResourceKey<Item> id = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(TheOneProbe.MODID, name));
        return new Item.Properties().setId(id);
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
