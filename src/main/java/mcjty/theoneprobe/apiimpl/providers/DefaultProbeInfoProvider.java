package mcjty.theoneprobe.apiimpl.providers;

import cofh.api.energy.IEnergyHandler;
import mcjty.lib.tools.ItemStackTools;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;
import static mcjty.theoneprobe.api.TextStyleClass.*;

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

        showStandardBlockInfo(config, mode, probeInfo, blockState, block, world, pos, player, data);

        if (Tools.show(mode, config.getShowCropPercentage())) {
            showGrowthLevel(probeInfo, blockState, block);
        }

        boolean showHarvestLevel = Tools.show(mode, config.getShowHarvestLevel());
        boolean showHarvested = Tools.show(mode, config.getShowCanBeHarvested());
        if (showHarvested && showHarvestLevel) {
            HarvestInfoTools.showHarvestInfo(probeInfo, world, pos, block, blockState, player);
        } else if (showHarvestLevel) {
            HarvestInfoTools.showHarvestLevel(probeInfo, blockState, block);
        } else if (showHarvested) {
            HarvestInfoTools.showCanBeHarvested(probeInfo, world, pos, block, player);
        }

        if (Tools.show(mode, config.getShowRedstone())) {
            showRedstonePower(probeInfo, world, blockState, data, block, Tools.show(mode, config.getShowLeverSetting()));
        }
        if (Tools.show(mode, config.getShowLeverSetting())) {
            showLeverSetting(probeInfo, world, blockState, data, block);
        }

        ChestInfoTools.showChestInfo(mode, probeInfo, world, pos, config);

        if (config.getRFMode() > 0) {
            showRF(probeInfo, world, pos);
        }
        if (Tools.show(mode, config.getShowTankSetting())) {
            if (config.getTankMode() > 0) {
                showTankInfo(probeInfo, world, pos);
            }
        }

        if (Tools.show(mode, config.getShowBrewStandSetting())) {
            showBrewingStandInfo(probeInfo, world, data, block);
        }
    }

    private void showBrewingStandInfo(IProbeInfo probeInfo, World world, IProbeHitData data, Block block) {
        if (block instanceof BlockBrewingStand) {
            TileEntity te = world.getTileEntity(data.getPos());
            if (te instanceof TileEntityBrewingStand) {
                int brewtime = ((TileEntityBrewingStand) te).getField(0);
                int fuel = ((TileEntityBrewingStand) te).getField(1);
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(new ItemStack(Items.BLAZE_POWDER), probeInfo.defaultItemStyle().width(16).height(16))
                        .text(LABEL + "Fuel: " + INFO + fuel);
                if (brewtime > 0) {
                    probeInfo.text(LABEL + "Time: " + INFO + brewtime + " ticks");
                }

            }
        }
    }

    private void showRedstonePower(IProbeInfo probeInfo, World world, IBlockState blockState, IProbeHitData data, Block block,
                                   boolean showLever) {
        if (showLever && block instanceof BlockLever) {
            // We are showing the lever setting so we don't show redstone in that case
            return;
        }
        int redstonePower;
        if (block instanceof BlockRedstoneWire) {
            redstonePower = blockState.getValue(BlockRedstoneWire.POWER);
        } else {
            redstonePower = world.getRedstonePower(data.getPos(), data.getSideHit().getOpposite());
        }
        if (redstonePower > 0) {
            probeInfo.horizontal()
                    .item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text(LABEL + "Power: " + INFO + redstonePower);
        }
    }

    private void showLeverSetting(IProbeInfo probeInfo, World world, IBlockState blockState, IProbeHitData data, Block block) {
        if (block instanceof BlockLever) {
            Boolean powered = blockState.getValue(BlockLever.POWERED);
            probeInfo.horizontal().item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text(LABEL + "State: " + INFO + (powered ? "On" : "Off"));
        } else if (block instanceof BlockRedstoneComparator) {
            BlockRedstoneComparator.Mode mode = blockState.getValue(BlockRedstoneComparator.MODE);
            probeInfo.text(LABEL + "Mode: " + INFO + mode.getName());
        } else if (block instanceof BlockRedstoneRepeater) {
            Boolean locked = blockState.getValue(BlockRedstoneRepeater.LOCKED);
            Integer delay = blockState.getValue(BlockRedstoneRepeater.DELAY);
            probeInfo.text(LABEL + "Delay: " + INFO + delay + " ticks");
            if (locked) {
                probeInfo.text(INFO + "Locked");
            }
        }
    }

    private void showTankInfo(IProbeInfo probeInfo, World world, BlockPos pos) {
        ProbeConfig config = Config.getDefaultConfig();
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            net.minecraftforge.fluids.capability.IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if (handler != null) {
                IFluidTankProperties[] properties = handler.getTankProperties();
                if (properties != null) {
                    for (IFluidTankProperties property : properties) {
                        if (property != null) {
                            FluidStack fluidStack = property.getContents();
                            int maxContents = property.getCapacity();
                            addFluidInfo(probeInfo, config, fluidStack, maxContents);
                        }
                    }
                }
            }
        }
    }

    private void addFluidInfo(IProbeInfo probeInfo, ProbeConfig config, FluidStack fluidStack, int maxContents) {
        int contents = fluidStack == null ? 0 : fluidStack.amount;
        if (fluidStack != null) {
            probeInfo.text(NAME + "Liquid: " + fluidStack.getLocalizedName());
        }
        if (config.getTankMode() == 1) {
            probeInfo.progress(contents, maxContents,
                    probeInfo.defaultProgressStyle()
                            .suffix("mB")
                            .filledColor(Config.tankbarFilledColor)
                            .alternateFilledColor(Config.tankbarAlternateFilledColor)
                            .borderColor(Config.tankbarBorderColor)
                            .numberFormat(Config.tankFormat));
        } else {
            probeInfo.text(PROGRESS + ElementProgress.format(contents, Config.tankFormat, "mB"));
        }
    }

    private void showRF(IProbeInfo probeInfo, World world, BlockPos pos) {
        ProbeConfig config = Config.getDefaultConfig();
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEnergyHandler) {
            IEnergyHandler handler = (IEnergyHandler) te;
            int energy = handler.getEnergyStored(EnumFacing.DOWN);
            int maxEnergy = handler.getMaxEnergyStored(EnumFacing.DOWN);
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, null)) {
            net.minecraftforge.energy.IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, null);
            if (handler != null) {
                addRFInfo(probeInfo, config, handler.getEnergyStored(), handler.getMaxEnergyStored());
            }
//        } else if (Loader.isModLoaded("tesla") && te != null && te.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null)) {
//            net.darkhax.tesla.api.ITeslaHolder handler = te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
//            if (handler != null) {
//                addRFInfo(probeInfo, config, handler.getStoredPower(), handler.getCapacity());
//            }
        }
    }

    private void addRFInfo(IProbeInfo probeInfo, ProbeConfig config, long energy, long maxEnergy) {
        if (config.getRFMode() == 1) {
            probeInfo.progress(energy, maxEnergy,
                    probeInfo.defaultProgressStyle()
                            .suffix("RF")
                            .filledColor(Config.rfbarFilledColor)
                            .alternateFilledColor(Config.rfbarAlternateFilledColor)
                            .borderColor(Config.rfbarBorderColor)
                            .numberFormat(Config.rfFormat));
        } else {
            probeInfo.text(PROGRESS + "RF: " + ElementProgress.format(energy, Config.rfFormat, "RF"));
        }
    }

    private void showGrowthLevel(IProbeInfo probeInfo, IBlockState blockState, Block block) {
        if (block instanceof BlockCrops) {
            BlockCrops crops = (BlockCrops) block;
            int age = crops.getAge(blockState);
            int maxAge = crops.getMaxAge();
            if (age == maxAge) {
                probeInfo.text(OK + "Fully grown");
            } else {
                probeInfo.text(LABEL + "Growth: " + WARNING + (age * 100) / maxAge + "%");
            }
        } else if (block instanceof BlockNetherWart) {
            BlockNetherWart wart = (BlockNetherWart) block;
            int age = blockState.getValue(BlockNetherWart.AGE);
            int maxAge = 3;
            if (age == maxAge) {
                probeInfo.text(OK + "Fully grown");
            } else {
                probeInfo.text(LABEL + "Growth: " + WARNING + (age * 100) / maxAge + "%");
            }
        }
    }

    public static void showStandardBlockInfo(IProbeConfig config, ProbeMode mode, IProbeInfo probeInfo, IBlockState blockState, Block block, World world,
                                             BlockPos pos, EntityPlayer player, IProbeHitData data) {
        String modid = Tools.getModName(block);

        if (block instanceof BlockSilverfish && mode != ProbeMode.DEBUG) {
            BlockSilverfish.EnumType type = blockState.getValue(BlockSilverfish.VARIANT);
            blockState = type.getModelBlock();
            block = blockState.getBlock();
        }

        if (block instanceof BlockLiquid) {
            Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
            if (fluid != null) {
                FluidStack stack = new FluidStack(fluid, 1);
                probeInfo.horizontal()
                        .icon(fluid.getStill(), -1, -1, 16, 16, probeInfo.defaultIconStyle().width(20))
                        .vertical()
                        .text(NAME + stack.getLocalizedName())
                        .text(MODNAME + modid);
                return;
            }
        }

        ItemStack pickBlock = data.getPickBlock();
        if (ItemStackTools.isValid(pickBlock)) {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.horizontal()
                        .item(pickBlock)
                        .vertical()
                        .text(NAME + getStackUnlocalizedName(pickBlock))
                        .text(MODNAME + modid);
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(pickBlock)
                        .text(NAME + getStackUnlocalizedName(pickBlock));

            }
        } else {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.vertical()
                        .text(NAME + getBlockUnlocalizedName(block))
                        .text(MODNAME + modid);
            } else {
                probeInfo.vertical()
                        .text(NAME + getBlockUnlocalizedName(block));
            }
        }
    }

    private static String getBlockUnlocalizedName(Block block) {
        return STARTLOC + block.getUnlocalizedName() + ".name" + ENDLOC;
    }

    private static String getStackUnlocalizedName(ItemStack stack) {
        NBTTagCompound nbttagcompound = getSubCompound(stack, "display");

        if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("Name", 8)) {
                return nbttagcompound.getString("Name");
            }

            if (nbttagcompound.hasKey("LocName", 8)) {
                return STARTLOC + nbttagcompound.getString("LocName") + ENDLOC;
            }
        }

        return STARTLOC + stack.getItem().getUnlocalizedNameInefficiently(stack) + ".name" + ENDLOC;
    }

    private static NBTTagCompound getSubCompound(ItemStack stack, String key) {
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey(key, 10)) {
            return stack.getTagCompound().getCompoundTag(key);
        } else {
            return null;
        }
    }


}
