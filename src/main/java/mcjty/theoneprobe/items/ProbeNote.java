package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.setup.GuiProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProbeNote extends Item {

    public ProbeNote() {
        setUnlocalizedName(TheOneProbe.MODID + ".probenote");
        setRegistryName("probenote");
        setCreativeTab(TheOneProbe.tabProbe);
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            if (player.isSneaking()) {
                player.openGui(TheOneProbe.instance, GuiProxy.GUI_CONFIG, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
            } else {
                player.openGui(TheOneProbe.instance, GuiProxy.GUI_NOTE, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
