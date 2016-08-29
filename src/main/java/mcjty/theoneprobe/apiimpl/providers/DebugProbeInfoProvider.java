package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":debug";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (mode == ProbeMode.DEBUG && Config.showDebugInfo) {
            Block block = blockState.getBlock();
            BlockPos pos = data.getPos();
            showDebugInfo(probeInfo, world, blockState, pos, block, data.getSideHit());
        }
    }

    private void showDebugInfo(IProbeInfo probeInfo, World world, IBlockState blockState, BlockPos pos, Block block, EnumFacing side) {
        IProbeInfo vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffff4444).spacing(2))
                .text("Unlocname: " + block.getUnlocalizedName())
                .text("Meta: " + blockState.getBlock().getMetaFromState(blockState))
                .text("Hardness: " + block.getBlockHardness(blockState, world, pos))
                .text("Weak power: " + block.getWeakPower(blockState, world, pos, side.getOpposite()))
                .text("Strong power: " + block.getStrongPower(blockState, world, pos, side.getOpposite()))
                .text("Light: " + block.getLightValue(blockState, world, pos))
                ;
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            vertical.text("TE: " + te.getClass().getSimpleName());
        }
    }
}
