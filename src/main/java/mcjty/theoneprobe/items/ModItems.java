package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.compat.BaubleTools;
import mcjty.theoneprobe.setup.ModSetup;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static CreativeProbe creativeProbe;
    public static Probe probe;
    public static Item diamondHelmetProbe;
    public static Item goldHelmetProbe;
    public static Item ironHelmetProbe;
    public static Item probeGoggles;
    public static ProbeNote probeNote;

    public static String PROBETAG = "theoneprobe";
    public static String PROBETAG_HAND = "theoneprobe_hand";

    public static void init() {
        probe = new Probe();
        creativeProbe = new CreativeProbe();

        ItemArmor.ArmorMaterial materialDiamondHelmet = EnumHelper.addArmorMaterial("diamond_helmet_probe", TheOneProbe.MODID + ":probe_diamond",
                33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
        ItemArmor.ArmorMaterial materialGoldHelmet = EnumHelper.addArmorMaterial("gold_helmet_probe", TheOneProbe.MODID + ":probe_gold",
                7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);
        ItemArmor.ArmorMaterial materialIronHelmet = EnumHelper.addArmorMaterial("iron_helmet_probe", TheOneProbe.MODID + ":probe_iron",
                15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

        diamondHelmetProbe = makeHelmet(materialDiamondHelmet, 3, "diamond_helmet_probe");
        goldHelmetProbe = makeHelmet(materialGoldHelmet, 4, "gold_helmet_probe");
        ironHelmetProbe = makeHelmet(materialIronHelmet, 2, "iron_helmet_probe");

        probeNote = new ProbeNote();

        if (ModSetup.baubles) {
            probeGoggles = BaubleTools.initProbeGoggle();
        }
    }

    private static Item makeHelmet(ItemArmor.ArmorMaterial material, int renderIndex, String name) {
        Item item = new ItemArmor(material, renderIndex, EntityEquipmentSlot.HEAD) {
            @Override
            public boolean getHasSubtypes() {
                return true;
            }

            @Override
            public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
                if (this.isInCreativeTab(tab)) {
                    ItemStack stack = new ItemStack(this);
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setInteger(PROBETAG, 1);
                    stack.setTagCompound(tag);
                    subItems.add(stack);
                }
            }
        };
        item.setUnlocalizedName(TheOneProbe.MODID + "." + name);
        item.setRegistryName(name);
        item.setCreativeTab(TheOneProbe.tabProbe);
        return item;
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        probe.initModel();
        creativeProbe.initModel();

        ModelLoader.setCustomModelResourceLocation(diamondHelmetProbe, 0, new ModelResourceLocation(diamondHelmetProbe.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(goldHelmetProbe, 0, new ModelResourceLocation(goldHelmetProbe.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ironHelmetProbe, 0, new ModelResourceLocation(ironHelmetProbe.getRegistryName(), "inventory"));

        probeNote.initModel();

        if (ModSetup.baubles) {
            BaubleTools.initProbeModel(probeGoggles);
        }
    }

    public static boolean isProbeInHand(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getItem() == probe || stack.getItem() == creativeProbe) {
            return true;
        }
        if (stack.getTagCompound() == null) {
            return false;
        }
        return stack.getTagCompound().hasKey(PROBETAG_HAND);
    }

    private static boolean isProbeHelmet(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getTagCompound() == null) {
            return false;
        }
        return stack.getTagCompound().hasKey(PROBETAG);
    }

    public static boolean hasAProbeSomewhere(EntityPlayer player) {
        return hasProbeInHand(player, EnumHand.MAIN_HAND) || hasProbeInHand(player, EnumHand.OFF_HAND) || hasProbeInHelmet(player)
                || hasProbeInBauble(player);
    }

    private static boolean hasProbeInHand(EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);
        return isProbeInHand(item);
    }

    private static boolean hasProbeInHelmet(EntityPlayer player) {
        ItemStack helmet = player.inventory.getStackInSlot(36+3);
//        ItemStack helmet = player.inventory.armorInventory.get(3);
        return isProbeHelmet(helmet);
    }

    private static boolean hasProbeInBauble(EntityPlayer player) {
        if (ModSetup.baubles) {
            return BaubleTools.hasProbeGoggle(player);
        } else {
            return false;
        }
    }
}
