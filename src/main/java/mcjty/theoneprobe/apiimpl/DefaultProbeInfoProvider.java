package mcjty.theoneprobe.apiimpl;

import cofh.api.energy.IEnergyHandler;
import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class DefaultProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":default";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        Block block = blockState.getBlock();
        BlockPos pos = data.getPos();

        showStandardBlockInfo(probeInfo, blockState, block, world, pos);

        if (Tools.show(mode, Config.showRedstone)) {
            showRedstonePower(probeInfo, world, blockState, data, block);
        }
        if (Tools.show(mode, Config.showCropPercentage)) {
            showGrowthLevel(probeInfo, blockState, block);
        }
        if (Tools.show(mode, Config.showHarvestLevel)) {
            showHarvestLevel(probeInfo, blockState, block);
        }
        if (Tools.show(mode, Config.showChestContents)) {
            showChestContents(probeInfo, world, pos);
        }

        if (Config.showRF > 0) {
            showRF(probeInfo, world, pos);
        }
    }

    private void showRedstonePower(IProbeInfo probeInfo, World world, IBlockState blockState, IProbeHitData data, Block block) {
        int redstonePower;
        if (block instanceof BlockRedstoneWire) {
            redstonePower = blockState.getValue(BlockRedstoneWire.POWER);
        } else {
            redstonePower = world.getRedstonePower(data.getPos(), data.getSideHit().getOpposite());
        }
        if (redstonePower > 0) {
            probeInfo.text("Power: " + redstonePower);
        }
    }

    private void showRF(IProbeInfo probeInfo, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEnergyHandler) {
            IEnergyHandler handler = (IEnergyHandler) te;
            int energy = handler.getEnergyStored(EnumFacing.DOWN);
            int maxEnergy = handler.getMaxEnergyStored(EnumFacing.DOWN);
            if (Config.showRF == 1) {
                probeInfo.progress(energy, maxEnergy,
                        probeInfo.defaultProgressStyle()
                                .suffix("RF")
                                .filledColor(Config.rfbarFilledColor)
                                .alternateFilledColor(Config.rfbarAlternateFilledColor)
                                .borderColor(Config.rfbarBorderColor)
                                .numberFormat(Config.rfFormat));
            } else {
                probeInfo.text(TextFormatting.GREEN + "RF: " + ElementProgress.format(energy, Config.rfFormat) + "RF");
            }
        }
    }

    private void showHarvestLevel(IProbeInfo probeInfo, IBlockState blockState, Block block) {
        String harvestTool = block.getHarvestTool(blockState);
        if (harvestTool != null) {
            probeInfo.text(TextFormatting.GREEN + "Harvest tool: " + harvestTool);
        }
    }

    private void showGrowthLevel(IProbeInfo probeInfo, IBlockState blockState, Block block) {
        if (block instanceof BlockCrops) {
            BlockCrops crops = (BlockCrops) block;
            int maxAge = crops.getMaxAge();
            Integer age = blockState.getValue(BlockCrops.AGE);
            probeInfo.text(TextFormatting.GREEN + "Growth: " + (age * 100) / maxAge + "%");
        }
    }

    private void showStandardBlockInfo(IProbeInfo probeInfo, IBlockState blockState, Block block, World world, BlockPos pos) {
        String modid = Tools.getModName(block);
        Item item;

        ItemStack pickBlock = block.getPickBlock(blockState, null, world, pos, null);
        if (pickBlock != null) {
            showStack(probeInfo, modid, pickBlock);
        } else {
            probeInfo.vertical()
                    .text(TextFormatting.WHITE + block.getLocalizedName())
                    .text(TextFormatting.BLUE + modid);
        }
    }

    private void showStack(IProbeInfo probeInfo, String modid, ItemStack stack) {
        probeInfo.horizontal()
                .item(stack)
                .vertical()
                    .text(TextFormatting.WHITE + stack.getDisplayName())
                    .text(TextFormatting.BLUE + modid);
    }

    private void showChestContents(IProbeInfo probeInfo, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        IProbeInfo vertical = null;
        IProbeInfo horizontal = null;
        int rows = 0;
        int idx = 0;
        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler capability = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = 0 ; i < capability.getSlots() ; i++) {
                ItemStack stackInSlot = capability.getStackInSlot(i);
                if (stackInSlot != null) {
                    if (idx % 10 == 0) {
                        if (vertical == null) {
                            vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xffffffff).spacing(0));
                        }
                        horizontal = vertical.horizontal(new LayoutStyle().spacing(0));
                        rows++;
                        if (rows > 4) {
                            break;
                        }
                    }
                    horizontal.item(stackInSlot);
                    idx++;
                }
            }
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            for (int i = 0 ; i < inventory.getSizeInventory() ; i++) {
                ItemStack stackInSlot = inventory.getStackInSlot(i);
                if (stackInSlot != null) {
                    if (idx % 10 == 0) {
                        if (vertical == null) {
                            vertical = probeInfo.vertical(new LayoutStyle().borderColor(0xffffffff).spacing(0));
                        }
                        horizontal = vertical.horizontal(new LayoutStyle().spacing(0));
                        rows++;
                        if (rows > 4) {
                            break;
                        }
                    }
                    horizontal.item(stackInSlot);
                    idx++;
                }
            }
        }
    }

}
