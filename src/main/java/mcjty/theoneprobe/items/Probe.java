package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class Probe extends Item {

    public Probe() {
        super(new Properties()
                .maxStackSize(1)
                .group(TheOneProbe.tabProbe));
        setRegistryName("probe");
    }

    public void initModel() {
//        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            ProbeClient.displayProbeGui();
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

}
