package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class DefaultProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public boolean needsServerInfo(World world, IBlockState blockState, BlockPos pos) {
        return false;
    }

    @Override
    public void addProbeInfo(IProbeInfo probeInfo, World world, IBlockState blockState, BlockPos pos, EntityPlayer player) {
        Block block = blockState.getBlock();
        String modid = getModName(block);

        Item item = Item.getItemFromBlock(block);
        if (item != null) {
            ItemStack stack = new ItemStack(item, 1);
            probeInfo.item(stack)
                    .text(TextFormatting.WHITE + stack.getDisplayName())
                    .newline()
                    .offset(20, -10)
                    .text(TextFormatting.BLUE + modid)
                    .newline();
        } else {
            probeInfo.text(TextFormatting.WHITE + block.getLocalizedName())
                    .newline()
                    .offset(20, -10)
                    .text(TextFormatting.BLUE + modid)
                    .newline();
        }
        probeInfo.progress(30, 100)
                .newline();
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
