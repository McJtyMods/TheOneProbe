package mcjty.theoneprobe.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ProbeGoggles extends Item /*implements IBauble*/ {

    public ProbeGoggles() {
        super(new Settings().itemGroup(ItemGroup.DECORATIONS));

//        setUnlocalizedName(TheOneProbe.MODID + ".probe_goggles");
//        setRegistryName("probe_goggles");
//        setCreativeTab(TheOneProbe.tabProbe);
    }

//    @Override
//    public BaubleType getBaubleType(ItemStack itemstack) {
//        return BaubleType.HEAD;
//    }
//
//    @Override
//    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
//
//    }
//
//    @Override
//    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
//
//    }
//
//    @Override
//    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
//
//    }
//
//    @Override
//    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
//        return true;
//    }
//
//    @Override
//    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
//        return true;
//    }
//
//    @Override
//    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
//        return false;
//    }
}
