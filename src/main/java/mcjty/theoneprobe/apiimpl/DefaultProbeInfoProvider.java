package mcjty.theoneprobe.apiimpl;

import cofh.api.energy.IEnergyHandler;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProgressStyle;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class DefaultProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public void addProbeInfo(IProbeInfo probeInfo, World world, IBlockState blockState, BlockPos pos) {
        Block block = blockState.getBlock();
        String modid = getModName(block);

        Item item = Item.getItemFromBlock(block);
        if (item != null) {
            ItemStack stack = new ItemStack(item, 1);
            probeInfo.horizontal()
                    .item(stack)
                    .vertical()
                        .text(TextFormatting.WHITE + stack.getDisplayName())
                        .text(TextFormatting.BLUE + modid);
        } else {
            probeInfo.horizontal()
                    .text(TextFormatting.WHITE + block.getLocalizedName())
                    .text(TextFormatting.BLUE + modid);
        }
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEnergyHandler) {
            IEnergyHandler handler = (IEnergyHandler) te;
            probeInfo.progress(handler.getEnergyStored(EnumFacing.DOWN), handler.getMaxEnergyStored(EnumFacing.DOWN), "", "RF",
                    new ProgressStyle().filledColor(0xffdd0000).alternateFilledColor(0xff430000).borderColor(0xff555555));
        }
    }

    private String getModName(Block block) {
        String modid = block.getRegistryName().getResourceDomain();
        for (ModContainer container : Loader.instance().getActiveModList()) {
            if (modid.equals(container.getModId())) {
                modid = container.getName();
                break;
            }
        }
        return modid;
    }
}
