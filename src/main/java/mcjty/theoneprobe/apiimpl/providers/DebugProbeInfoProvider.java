package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;

public class DebugProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(TheOneProbe.MODID, "debug");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
        if (mode == ProbeMode.DEBUG && Config.showDebugInfo.get()) {
            BlockPos pos = data.getPos();
            showDebugInfo(probeInfo, world, blockState, pos, data.getSideHit());
        }
    }

    private void showDebugInfo(IProbeInfo probeInfo, Level world, BlockState blockState, BlockPos pos, Direction side) {
        Block block = blockState.getBlock();
        String simpleName = block.getClass().getSimpleName();
        IProbeInfo vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2))
                .text(CompoundText.createLabelInfo("Reg Name: ", BuiltInRegistries.BLOCK.getKey(block).toString()))
                .text(CompoundText.createLabelInfo("Unlocname: ", block.getDescriptionId()))
                .text(CompoundText.createLabelInfo("Class: ", simpleName))
                .text(CompoundText.createLabelInfo("Hardness: ", blockState.getDestroySpeed(world, pos)))
                .text(CompoundText.createLabelInfo("Power W: ",+ blockState.getSignal(world, pos, side.getOpposite()))
                        .style(LABEL).text(", S: ").style(INFO).text(String.valueOf(blockState.getDirectSignal(world, pos, side.getOpposite()))))
                .text(CompoundText.createLabelInfo("Light: ", block.getLightEmission(blockState, world, pos)));
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null) {
            vertical.text(CompoundText.createLabelInfo("TE: ", te.getClass().getSimpleName()));
        }
    }
}
