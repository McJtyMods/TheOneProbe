package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BlockProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public ResourceLocation getID() {
        return ResourceLocation.fromNamespaceAndPath(TheOneProbe.MODID, "block");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
        Block block = blockState.getBlock();
        if (block instanceof IProbeInfoAccessor accessor) {
            accessor.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        }
    }
}
