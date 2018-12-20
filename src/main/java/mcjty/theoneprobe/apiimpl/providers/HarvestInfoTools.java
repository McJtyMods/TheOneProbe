package mcjty.theoneprobe.apiimpl.providers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.fabricmc.fabric.tags.FabricItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static mcjty.theoneprobe.api.TextStyleClass.*;

public class HarvestInfoTools {

    private static final Identifier ICONS = new Identifier(TheOneProbe.MODID, "textures/gui/icons.png");
    private static String[] harvestLevels = new String[]{
            "stone",
            "iron",
            "diamond",
            "obsidian",
            "cobalt"
    };

    private static final Map<String, ItemStack> testTools = new HashMap<>();
    static {
        testTools.put("shovel", new ItemStack(Items.WOODEN_SHOVEL));
        testTools.put("axe", new ItemStack(Items.WOODEN_AXE));
        testTools.put("pickaxe", new ItemStack(Items.WOODEN_PICKAXE));
    }

    private static int checkHarvestability(Collection<Item> itemsToCheck, BlockState state) {
        int smallestNeededLevel = 100000;
        for (Item item : itemsToCheck) {
            ItemStack s = new ItemStack(item);
            if (state.getMaterial().canBreakByHand() || s.isEffectiveOn(state)) {
                if (item instanceof ToolItem) {
                    ToolMaterial type = ((ToolItem) item).getType();
                    int level = type.getMiningLevel();
                    if (level < smallestNeededLevel) {
                        smallestNeededLevel = level;
                    }
                }
            }
        }
        return smallestNeededLevel >= 100000 ? -1 : smallestNeededLevel;
    }

    private static class MiningInfo {
        private final String tool;
        private final int level;

        public MiningInfo(String tool, int level) {
            this.tool = tool;
            this.level = level;
        }
    }

    private static LoadingCache<BlockState, MiningInfo> miningInfoCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(CacheLoader.from((state) -> fetchMiningInfo(state)));

    private static MiningInfo fetchMiningInfo(BlockState blockState) {
        /*
         *     Wood:    0
         *     Stone:   1
         *     Iron:    2
         *     Diamond: 3
         */

        String harvestTool = null;
        int level = -1;
        level = checkHarvestability(FabricItemTags.AXES.values(), blockState);
        if (level != -1) {
            harvestTool = "axe";
        } else {
            level = checkHarvestability(FabricItemTags.PICKAXES.values(), blockState);
            if (level != -1) {
                harvestTool = "pickaxe";
            } else {
                level = checkHarvestability(FabricItemTags.SHOVELS.values(), blockState);
                if (level != -1) {
                    harvestTool = "shovel";
                } else {
                    level = checkHarvestability(FabricItemTags.HOES.values(), blockState);
                    if (level != -1) {
                        harvestTool = "hoe";
                    } else {
                        level = checkHarvestability(FabricItemTags.SWORDS.values(), blockState);
                        if (level != -1) {
                            harvestTool = "sword";
                        }
                    }
                }
            }
        }
        if (harvestTool == null) {
            return new MiningInfo(null, -1);
        } else {
            return new MiningInfo(harvestTool, level);
        }
    }

    static void showHarvestLevel(IProbeInfo probeInfo, BlockState blockState, Block block) {
        MiningInfo info = miningInfoCache.getUnchecked(blockState);
        if (info.tool != null) {
            int harvestLevel = info.level;
            String harvestName;
            if (harvestLevel >= harvestLevels.length) {
                harvestName = Integer.toString(harvestLevel);
            } else if (harvestLevel < 0) {
                harvestName = Integer.toString(harvestLevel);
            } else {
                harvestName = harvestLevels[harvestLevel];
            }
            probeInfo.text(LABEL + "Tool: " + INFO + info.tool + " (level " + harvestName + ")");
        }
    }

    private static boolean canHarvestBlock(@Nonnull Block block, @Nonnull PlayerEntity player, @Nonnull World world, @Nonnull BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getMaterial().canBreakByHand()) {
            return true;
        }
        ItemStack stack = player.getMainHandStack();
        return stack.isEffectiveOn(state);
//        MiningInfo info = miningInfoCache.getUnchecked(state);
//        String tool = info.tool;
//        if (stack.isEmpty() || tool == null) {
//            return true;
//        }
//
//        int toolLevel = info.level;
//        if (toolLevel < 0) {
//            return true;
//        }
//
//        return toolLevel >= block.getHarvestLevel(state);
    }

    static void showCanBeHarvested(IProbeInfo probeInfo, World world, BlockPos pos, Block block, PlayerEntity player) {
        if (ModItems.isProbeInHand(player.getMainHandStack())) {
            // If the player holds the probe there is no need to show harvestability information as the
            // probe cannot harvest anything. This is only supposed to work in off hand.
            return;
        }

        boolean harvestable = canHarvestBlock(block, player, world, pos);
//        boolean harvestable = false;// @todo fabric:  block.canHarvestBlock(world, pos, player) && world.getBlockState(pos).getBlockHardness(world, pos) >= 0;
        if (harvestable) {
            probeInfo.text(OK + "Harvestable");
        } else {
            probeInfo.text(WARNING + "Not harvestable");
        }
    }

    static void showHarvestInfo(IProbeInfo probeInfo, World world, BlockPos pos, Block block, BlockState blockState, PlayerEntity player) {
        boolean harvestable = canHarvestBlock(block, player, world, pos);
//        boolean harvestable = false;// @todo fabric: block.canHarvestBlock(world, pos, player) && world.getBlockState(pos).getBlockHardness(world, pos) >= 0;

        MiningInfo info = miningInfoCache.getUnchecked(blockState);
        String harvestTool = info.tool;
        String harvestName = null;

        if (harvestTool == null) {
            // The block doesn't have an explicitly-set harvest tool, so we're going to test our wooden tools against the block.
            float blockHardness = blockState.getHardness(world, pos);
            if (blockHardness > 0f) {
                for (Map.Entry<String, ItemStack> testToolEntry : testTools.entrySet()) {
                    // loop through our test tools until we find a winner.
                    ItemStack testTool = testToolEntry.getValue();

                    if (testTool != null && testTool.getItem() instanceof ToolItem) {
                        ToolItem toolItem = (ToolItem) testTool.getItem();
                        // @todo
                        if (testTool.getBlockBreakingSpeed(blockState) >= toolItem.getType().getBlockBreakingSpeed()) {
                            // BINGO!
                            harvestTool = testToolEntry.getKey();
                            break;
                        }
                    }
                }
            }
        }

        if (harvestTool != null) {
            int harvestLevel = info.level;
            if (harvestLevel < 0) {
                // NOTE: When a block doesn't have an explicitly-set harvest tool, getHarvestLevel will return -1 for ANY tool. (Expected behavior)
//                TheOneProbe.logger.info("HarvestLevel out of bounds (less than 0). Found " + harvestLevel);
            } else if (harvestLevel >= harvestLevels.length) {
//                TheOneProbe.logger.info("HarvestLevel out of bounds (Max value " + harvestLevels.length + "). Found " + harvestLevel);
            } else {
                harvestName = harvestLevels[harvestLevel];
            }
            harvestTool = StringUtils.capitalize(harvestTool);
        }

        boolean v = Config.harvestStyleVanilla;
        int offs = v ? 16 : 0;
        int dim = v ? 13 : 16;

        ILayoutStyle alignment = probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER);
        IIconStyle iconStyle = probeInfo.defaultIconStyle().width(v ? 18 : 20).height(v ? 14 : 16).textureWidth(32).textureHeight(32);
        IProbeInfo horizontal = probeInfo.horizontal(alignment);
        if (harvestable) {
            horizontal.icon(ICONS, 0, offs, dim, dim, iconStyle)
                    .text(OK + ((harvestTool != null) ? harvestTool : "No tool"));
        } else {
            if (harvestName == null || harvestName.isEmpty()) {
                horizontal.icon(ICONS, 16, offs, dim, dim, iconStyle)
                        .text(WARNING + ((harvestTool != null) ? harvestTool : "No tool"));
            } else {
                horizontal.icon(ICONS, 16, offs, dim, dim, iconStyle)
                        .text(WARNING + ((harvestTool != null) ? harvestTool : "No tool") + " (level " + harvestName + ")");
            }
        }
    }
}
