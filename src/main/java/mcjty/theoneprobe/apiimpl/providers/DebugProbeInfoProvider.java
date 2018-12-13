package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;

public class DebugProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":debug";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (mode == ProbeMode.DEBUG && Config.showDebugInfo) {
            Block block = blockState.getBlock();
            BlockPos pos = data.getPos();
            showDebugInfo(probeInfo, world, blockState, pos, block, data.getSideHit());
        }
    }

    private void showDebugInfo(IProbeInfo probeInfo, World world, BlockState blockState, BlockPos pos, Block block, Direction side) {
        String simpleName = block.getClass().getSimpleName();
        IProbeInfo vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2))
                .text(LABEL + "Reg Name: " + INFO + Registry.BLOCK.getId(block).toString())
                .text(LABEL + "Unlocname: " + INFO + block.getTranslationKey())
                .text(LABEL + "Class: " + INFO + simpleName)
                .text(LABEL + "Hardness: " + INFO + block.getHardness(blockState, world, pos))
                .text(LABEL + "Power W: " + INFO + block.getWeakRedstonePower(blockState, world, pos, side.getOpposite())
                        + LABEL + ", S: " + INFO + block.getStrongRedstonePower(blockState, world, pos, side.getOpposite()))
                .text(LABEL + "Light: " + INFO + block.getAmbientOcclusionLightLevel(blockState, world, pos));
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null) {
            vertical.text(LABEL + "TE: " + INFO + te.getClass().getSimpleName());
        }
    }
}
