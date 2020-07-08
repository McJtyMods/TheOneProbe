package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
        if (mode == ProbeMode.DEBUG && Config.showDebugInfo.get()) {
            BlockPos pos = data.getPos();
            showDebugInfo(probeInfo, world, blockState, pos, data.getSideHit());
        }
    }

    private void showDebugInfo(IProbeInfo probeInfo, World world, BlockState blockState, BlockPos pos, Direction side) {
        Block block = blockState.getBlock();
        String simpleName = block.getClass().getSimpleName();
        IProbeInfo vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2))
                .text(CompoundText.create().style(LABEL).text("Reg Name: ").style(INFO).text(block.getRegistryName().toString()).get())
                .text(CompoundText.create().style(LABEL).text("Unlocname: ").style(INFO).text(block.getTranslationKey()).get())
                .text(CompoundText.create().style(LABEL).text("Class: ").style(INFO).text(simpleName).get())
                .text(CompoundText.create().style(LABEL).text("Hardness: ").style(INFO).text(String.valueOf(blockState.getBlockHardness(world, pos))).get())
                .text(CompoundText.create().style(LABEL).text("Power W: ").style(INFO).text(String.valueOf(blockState.getWeakPower(world, pos, side.getOpposite())))
                      .style(LABEL).text(", S: ").style(INFO).text(String.valueOf(blockState.getStrongPower(world, pos, side.getOpposite()))).get())
                .text(CompoundText.create().style(LABEL).text("Light: ").style(INFO).text(String.valueOf(blockState.getLightValue(world, pos))).get());
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            vertical.text(CompoundText.create().style(LABEL).text("TE: ").style(INFO).text(te.getClass().getSimpleName()).get());
        }
    }
}
