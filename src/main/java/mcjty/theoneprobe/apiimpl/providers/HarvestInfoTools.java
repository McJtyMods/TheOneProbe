package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.lib.tag.TagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

import static mcjty.theoneprobe.api.TextStyleClass.OK;
import static mcjty.theoneprobe.api.TextStyleClass.WARNING;

public class HarvestInfoTools {

    private static final ResourceLocation ICONS = new ResourceLocation(TheOneProbe.MODID, "textures/gui/icons.png");

    private static String getTools(BlockState state) {
        Set<ResourceLocation> tags = TagHelper.getTags(state.getBlock());
        Map<ResourceLocation, String> tooltypes = Config.getTooltypeTags();
        String tools = "";
        for (ResourceLocation tag : tags) {
            String s = tooltypes.get(tag);
            if (s != null) {
                if (!tools.isEmpty()) {
                    tools += " ";
                }
                tools += s;
            }
        }
        return tools;
    }

    private static String getLevels(BlockState state) {
        Set<ResourceLocation> tags = TagHelper.getTags(state.getBlock());
        Map<ResourceLocation, String> harvestability = Config.getHarvestabilityTags();
        String levels = "";
        for (ResourceLocation tag : tags) {
            String s = harvestability.get(tag);
            if (s != null) {
                if (!levels.isEmpty()) {
                    levels += " ";
                }
                levels += s;
            }
        }
        return levels;
    }

    static void showHarvestLevel(IProbeInfo probeInfo, BlockState state) {
        String tools = getTools(state);
        String levels = getLevels(state);
        if (!tools.isEmpty() && !levels.isEmpty()) {
            probeInfo.text(CompoundText.createLabelInfo("Tools: ", tools + " (level " + levels + ")"));
        } else if (!tools.isEmpty()) {
            probeInfo.text(CompoundText.createLabelInfo("Tools: ", tools));
        } else if (!levels.isEmpty()) {
            probeInfo.text(CompoundText.createLabelInfo("Level: ", levels));
        }
    }

    static void showCanBeHarvested(IProbeInfo probeInfo, Level world, BlockPos pos, BlockState state, Player player) {
        if (ModItems.isProbeInHand(player.getMainHandItem())) {
            // If the player holds the probe there is no need to show harvestability information as the
            // probe cannot harvest anything. This is only supposed to work in off hand.
            return;
        }

        ItemStack stack = player.getMainHandItem();
        boolean harvestable = stack.getItem().isCorrectToolForDrops(state);
        if (harvestable) {
            probeInfo.text(CompoundText.create().style(OK).text("Harvestable"));
        } else {
            probeInfo.text(CompoundText.create().style(WARNING).text("Not harvestable"));
        }
    }

    static void showHarvestInfo(IProbeInfo probeInfo, Level world, BlockPos pos, Block block, BlockState blockState, Player player) {
        ItemStack stack = player.getMainHandItem();
        boolean harvestable = stack.getItem().isCorrectToolForDrops(blockState);
        String tools = getTools(blockState);
        String levels = getLevels(blockState);

        boolean v = Config.harvestStyleVanilla.get();
        int offs = v ? 16 : 0;
        int dim = v ? 13 : 16;

        ILayoutStyle alignment = probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER);
        IIconStyle iconStyle = probeInfo.defaultIconStyle().width(v ? 18 : 20).height(v ? 14 : 16).textureWidth(32).textureHeight(32);
        IProbeInfo horizontal = probeInfo.horizontal(alignment);
        if (harvestable) {
            horizontal.icon(ICONS, 0, offs, dim, dim, iconStyle)
                    .text(CompoundText.create().style(OK).text((tools.isEmpty() ? "No tool" : tools)));
        } else {
            if (levels.isEmpty()) {
                horizontal.icon(ICONS, 16, offs, dim, dim, iconStyle)
                        .text(CompoundText.create().style(WARNING).text((tools.isEmpty() ? "No tool" : tools)));
            } else {
                horizontal.icon(ICONS, 16, offs, dim, dim, iconStyle)
                        .text(CompoundText.create().style(WARNING).text(((tools.isEmpty() ? "No tool" : tools)) + " (level " + levels + ")"));
            }
        }
    }
}
