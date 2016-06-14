package mcjty.theoneprobe.apiimpl.providers;

import cofh.api.energy.IEnergyHandler;
import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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

        IProbeConfig config = Config.getRealConfig();

        showStandardBlockInfo(config, mode, probeInfo, blockState, block, world, pos, data.getSideHit(), data.getHitVec());

        if (Tools.show(mode, config.getShowCropPercentage())) {
            showGrowthLevel(probeInfo, blockState, block);
        }
        if (Tools.show(mode, config.getShowHarvestLevel())) {
            showHarvestLevel(probeInfo, blockState, block);
        }
        if (Tools.show(mode, config.getShowRedstone())) {
            showRedstonePower(probeInfo, world, blockState, data, block);
        }
        if (Tools.show(mode, config.getShowChestContents())) {
            showChestContents(probeInfo, world, pos);
        }

        if (config.getRFMode() > 0) {
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
            probeInfo.horizontal().item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14)).text("Power: " + redstonePower);
        }
    }

    private void showRF(IProbeInfo probeInfo, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEnergyHandler) {
            ProbeConfig config = Config.getDefaultConfig();
            IEnergyHandler handler = (IEnergyHandler) te;
            int energy = handler.getEnergyStored(EnumFacing.DOWN);
            int maxEnergy = handler.getMaxEnergyStored(EnumFacing.DOWN);
            if (config.getRFMode() == 1) {
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
            int age = crops.getAge(blockState);
            int maxAge = crops.getMaxAge();
            probeInfo.text(TextFormatting.GREEN + "Growth: " + (age * 100) / maxAge + "%");
        } else if (block instanceof BlockNetherWart) {
            BlockNetherWart wart = (BlockNetherWart) block;
            int age = blockState.getValue(BlockNetherWart.AGE);
            int maxAge = 3;
            probeInfo.text(TextFormatting.GREEN + "Growth: " + (age * 100) / maxAge + "%");
        }
    }

    private void showStandardBlockInfo(IProbeConfig config, ProbeMode mode, IProbeInfo probeInfo, IBlockState blockState, Block block, World world,
                                       BlockPos pos, EnumFacing sideHit, Vec3d hitVec) {
        String modid = Tools.getModName(block);

        if (block instanceof BlockLiquid) {
            Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
            if (fluid != null) {
                FluidStack stack = new FluidStack(fluid, 1);
                probeInfo.horizontal()
                        .icon(fluid.getStill(), -1, -1, 16, 16, probeInfo.defaultIconStyle().width(20))
                        .vertical()
                            .text(TextFormatting.WHITE + stack.getLocalizedName())
                            .text(TextFormatting.BLUE + modid);
                return;
            }
        }

        ItemStack pickBlock = block.getPickBlock(blockState, new RayTraceResult(hitVec, sideHit, pos), world, pos, null);
        if (pickBlock != null && pickBlock.getItem() != null) {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.horizontal()
                        .item(pickBlock)
                        .vertical()
                            .text(TextFormatting.WHITE + pickBlock.getDisplayName())
                            .text(TextFormatting.BLUE + modid);
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(pickBlock)
                        .text(TextFormatting.WHITE + pickBlock.getDisplayName());

            }
        } else {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.vertical()
                        .text(TextFormatting.WHITE + block.getLocalizedName())
                        .text(TextFormatting.BLUE + modid);
            } else {
                probeInfo.vertical()
                        .text(TextFormatting.WHITE + block.getLocalizedName());
            }
        }
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
