package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

public class HarvestInfoTools {

    private static final ResourceLocation ICONS = new ResourceLocation(TheOneProbe.MODID, "textures/gui/icons.png");
    private static String[] harvestLevels = new String[]{
            "stone",
            "iron",
            "diamond",
            "obsidian",
            "cobalt"
    };

    static void showHarvestLevel(IProbeInfo probeInfo, IBlockState blockState, Block block) {
        String harvestTool = block.getHarvestTool(blockState);
        if (harvestTool != null) {
            int harvestLevel = block.getHarvestLevel(blockState);
            String harvestName;
            if (harvestLevel >= harvestLevels.length) {
                harvestName = Integer.toString(harvestLevel);
            } else if (harvestLevel < 0) {
                harvestName = Integer.toString(harvestLevel);
            } else {
                harvestName = harvestLevels[harvestLevel];
            }
            probeInfo.text(TextFormatting.GREEN + "Tool: " + harvestTool + " (level " + harvestName + ")");
        }
    }

    static void showCanBeHarvested(IProbeInfo probeInfo, World world, BlockPos pos, Block block, EntityPlayer player) {
        if (ModItems.isProbeInHand(player.getHeldItemMainhand())) {
            // If the player holds the probe there is no need to show harvestability information as the
            // probe cannot harvest anything. This is only supposed to work in off hand.
            return;
        }

        boolean harvestable = block.canHarvestBlock(world, pos, player) && world.getBlockState(pos).getBlockHardness(world, pos) >= 0;
        if (harvestable) {
            probeInfo.text(TextFormatting.GREEN + "Harvestable");
        } else {
            probeInfo.text(TextFormatting.YELLOW + "Not harvestable");
        }
    }

    static void showHarvestInfo(IProbeInfo probeInfo, World world, BlockPos pos, Block block, IBlockState blockState, EntityPlayer player) {
        boolean harvestable = block.canHarvestBlock(world, pos, player) && world.getBlockState(pos).getBlockHardness(world, pos) >= 0;

        String harvestTool = block.getHarvestTool(blockState);
        String harvestName = null;
        if (harvestTool != null) {
            int harvestLevel = block.getHarvestLevel(blockState);
            if (harvestLevel >= harvestLevels.length) {
                harvestName = Integer.toString(harvestLevel);
            } else if (harvestLevel < 0) {
                harvestName = Integer.toString(harvestLevel);
            } else {
                harvestName = harvestLevels[harvestLevel];
            }
            harvestTool = StringUtils.capitalize(harvestTool);
        }

        ILayoutStyle alignment = probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER);
        IIconStyle iconStyle = probeInfo.defaultIconStyle().width(20).textureWidth(32).textureHeight(32);
        IProbeInfo horizontal = probeInfo.horizontal(alignment);
        if (harvestable) {
            horizontal.icon(ICONS, 0, 16, 13, 13, iconStyle)
                    .text(TextFormatting.GREEN + ((harvestTool != null) ? harvestTool : "No tool"));
        } else {
            if (harvestName == null || harvestName.isEmpty()) {
                horizontal.icon(ICONS, 16, 16, 13, 13, iconStyle)
                        .text(TextFormatting.YELLOW + ((harvestTool != null) ? harvestTool : "No tool"));
            } else {
                horizontal.icon(ICONS, 16, 16, 13, 13, iconStyle)
                        .text(TextFormatting.YELLOW + ((harvestTool != null) ? harvestTool : "No tool") + " (" + harvestName + ")");
            }
        }
    }
}
