package mcjty.theoneprobe.lib.tag;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class TagHelper {
    public static final java.util.Set<net.minecraft.resources.ResourceLocation> getTags(Block block) {
        return new ReverseTagWrapper<>(block, BlockTags::getAllTags).getTagNames();
    }
}
