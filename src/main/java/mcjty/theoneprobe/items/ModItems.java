package mcjty.theoneprobe.items;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeItem;
import mcjty.theoneprobe.compat.BaubleTools;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

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

    static {
        RecipeSorter.register("theoneprobe:addproberecipe", AddProbeRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
    }

    public static void init() {
        probe = new Probe();
        creativeProbe = new CreativeProbe();

        ItemArmor.ArmorMaterial materialDiamondHelmet = EnumHelper.addArmorMaterial("diamond_helmet_probe", TheOneProbe.MODID + ":probe_diamond",
                33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
        ItemArmor.ArmorMaterial materialGoldHelmet = EnumHelper.addArmorMaterial("gold_helmet_probe", TheOneProbe.MODID + ":probe_gold",
                7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);
        ItemArmor.ArmorMaterial materialIronHelmet = EnumHelper.addArmorMaterial("iron_helmet_probe", TheOneProbe.MODID + ":probe_iron",
                15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

        diamondHelmetProbe = makeHelpmet(materialDiamondHelmet, 3, "diamond_helmet_probe");
        goldHelmetProbe = makeHelpmet(materialGoldHelmet, 4, "gold_helmet_probe");
        ironHelmetProbe = makeHelpmet(materialIronHelmet, 2, "iron_helmet_probe");

        probeNote = new ProbeNote();

        if (TheOneProbe.baubles) {
            probeGoggles = BaubleTools.initProbeGoggle();
        }
    }

    private static Item makeHelpmet(ItemArmor.ArmorMaterial material, int renderIndex, String name) {
        Item item = new ItemProbedArmor(material, renderIndex, EntityEquipmentSlot.HEAD) {
            @Override
            public boolean canWorkAsProbe(ItemStack stack, EntityPlayer player) {
                return player.inventory.armorInventory[3] == stack;
            }

            @Override
            public boolean getHasSubtypes() {
                return true;
            }

            @Override
            public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
                ItemStack stack = new ItemStack(itemIn);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger(PROBETAG, 1);
                stack.setTagCompound(tag);
                subItems.add(stack);
            }
        };
        item.setUnlocalizedName(TheOneProbe.MODID + "." + name);
        item.setRegistryName(name);
        item.setCreativeTab(TheOneProbe.tabProbe);
        GameRegistry.register(item);
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

        if (TheOneProbe.baubles) {
            BaubleTools.initProbeModel(probeGoggles);
        }
    }

    public static void initCrafting() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(probe, 1), "C  ", " n ", "  r", 'C', Items.COMPARATOR, 'n', "nuggetGold", 'r', "dustRedstone"));
        GameRegistry.addRecipe(new AddProbeRecipe(Items.DIAMOND_HELMET, diamondHelmetProbe));
        GameRegistry.addRecipe(new AddProbeRecipe(Items.GOLDEN_HELMET, goldHelmetProbe));
        GameRegistry.addRecipe(new AddProbeRecipe(Items.IRON_HELMET, ironHelmetProbe));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(probeNote, 1), "r  ", " p ", "  r", 'p', Items.PAPER, 'r', "dustRedstone"));

        if (TheOneProbe.baubles) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(probeGoggles, 1), " g ", "gpg", " g ", 'p', probe, 'g', "nuggetGold"));
        }
    }

    public static boolean canWorkAsProbe(ItemStack stack, EntityPlayer player, String nbtProbeTagName) {
        if (stack == null) {
            return false;
        }
        if (stack.getItem() instanceof IProbeItem) {
            return ((IProbeItem) stack.getItem()).canWorkAsProbe(stack, player);
        }
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey(nbtProbeTagName);
    }

    public static boolean hasAProbeSomewhere(EntityPlayer player) {
        return hasProbeInHand(player, EnumHand.MAIN_HAND) || hasProbeInHand(player, EnumHand.OFF_HAND) || hasProbeInHelmet(player)
                || hasProbeInBauble(player);
    }

    public static boolean hasProbeInHand(EntityPlayer player, EnumHand hand) {
        return canWorkAsProbe(player.getHeldItem(hand), player, PROBETAG_HAND);
    }

    public static boolean hasProbeInHelmet(EntityPlayer player) {
        return canWorkAsProbe(player.inventory.armorInventory[3], player, PROBETAG);
    }

    public static boolean hasProbeInBauble(EntityPlayer player) {
        if (TheOneProbe.baubles) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
            int slots = baubles.getSlots();
            for(int i = 0; i < slots; i++) {
                if(canWorkAsProbe(baubles.getStackInSlot(i), player, PROBETAG)) {
                    return true;
                }
            }
        }
        return false;
    }
}
