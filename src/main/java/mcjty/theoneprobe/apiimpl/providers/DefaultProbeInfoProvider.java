package mcjty.theoneprobe.apiimpl.providers;

import mcjty.lib.api.power.IBigPower;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.compat.RedstoneFluxTools;
import mcjty.theoneprobe.compat.TeslaTools;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.setup.ModSetup;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;
import static mcjty.theoneprobe.api.TextStyleClass.*;

import java.util.Collections;

public class DefaultProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":default";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        Block block = blockState.getBlock();
        BlockPos pos = data.getPos();

        IProbeConfig config = ConfigSetup.getRealConfig();

        boolean handled = false;
        for (IBlockDisplayOverride override : TheOneProbe.theOneProbeImp.getBlockOverrides()) {
            if (override.overrideStandardInfo(mode, probeInfo, player, world, blockState, data)) {
                handled = true;
                break;
            }
        }
        if (!handled) {
            showStandardBlockInfo(config, mode, probeInfo, blockState, block, world, pos, player, data);
        }

        if (Tools.show(mode, config.getShowCropPercentage())) {
            showGrowthLevel(probeInfo, blockState);
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

        if (Tools.show(mode, config.getShowMobSpawnerSetting())) {
            showMobSpawnerInfo(probeInfo, world, data, block);
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

    private void showMobSpawnerInfo(IProbeInfo probeInfo, World world, IProbeHitData data, Block block) {
        if (block instanceof BlockMobSpawner) {
            TileEntity te = world.getTileEntity(data.getPos());
            if (te instanceof TileEntityMobSpawner) {
                MobSpawnerBaseLogic logic = ((TileEntityMobSpawner) te).getSpawnerBaseLogic();
                String mobName = logic.getCachedEntity().getName();
                probeInfo.horizontal(probeInfo.defaultLayoutStyle()
                    .alignment(ElementAlignment.ALIGN_CENTER))
                    .text(LABEL + "Mob: " + INFO + mobName);
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
        ProbeConfig config = ConfigSetup.getDefaultConfig();
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
                            .filledColor(ConfigSetup.tankbarFilledColor)
                            .alternateFilledColor(ConfigSetup.tankbarAlternateFilledColor)
                            .borderColor(ConfigSetup.tankbarBorderColor)
                            .numberFormat(ConfigSetup.tankFormat));
        } else {
            probeInfo.text(PROGRESS + ElementProgress.format(contents, ConfigSetup.tankFormat, "mB"));
        }
    }

    private void showRF(IProbeInfo probeInfo, World world, BlockPos pos) {
        ProbeConfig config = ConfigSetup.getDefaultConfig();
        TileEntity te = world.getTileEntity(pos);
        if (ModSetup.tesla && TeslaTools.isEnergyHandler(te)) {
            long energy = TeslaTools.getEnergy(te);
            long maxEnergy = TeslaTools.getMaxEnergy(te);
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (te instanceof IBigPower) {
            long energy = ((IBigPower) te).getStoredPower();
            long maxEnergy = ((IBigPower) te).getCapacity();
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (ModSetup.redstoneflux && RedstoneFluxTools.isEnergyHandler(te)) {
            int energy = RedstoneFluxTools.getEnergy(te);
            int maxEnergy = RedstoneFluxTools.getMaxEnergy(te);
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, null)) {
            net.minecraftforge.energy.IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, null);
            if (handler != null) {
                addRFInfo(probeInfo, config, handler.getEnergyStored(), handler.getMaxEnergyStored());
            }
        }
    }

    private void addRFInfo(IProbeInfo probeInfo, ProbeConfig config, long energy, long maxEnergy) {
        if (config.getRFMode() == 1) {
            probeInfo.progress(energy, maxEnergy,
                    probeInfo.defaultProgressStyle()
                            .suffix("RF")
                            .filledColor(ConfigSetup.rfbarFilledColor)
                            .alternateFilledColor(ConfigSetup.rfbarAlternateFilledColor)
                            .borderColor(ConfigSetup.rfbarBorderColor)
                            .numberFormat(ConfigSetup.rfFormat));
        } else {
            probeInfo.text(PROGRESS + "RF: " + ElementProgress.format(energy, ConfigSetup.rfFormat, "RF"));
        }
    }

    private void showGrowthLevel(IProbeInfo probeInfo, IBlockState blockState) {
        for (IProperty<?> property : blockState.getProperties().keySet()) {
            if(!"age".equals(property.getName())) continue;
            if(property.getValueClass() == Integer.class) {
                IProperty<Integer> integerProperty = (IProperty<Integer>)property;
                int age = blockState.getValue(integerProperty);
                int maxAge = Collections.max(integerProperty.getAllowedValues());
                if (age == maxAge) {
                    probeInfo.text(OK + "Fully grown");
                } else {
                    probeInfo.text(LABEL + "Growth: " + WARNING + (age * 100) / maxAge + "%");
                }
            }
            return;
        }
    }

    public static void showStandardBlockInfo(IProbeConfig config, ProbeMode mode, IProbeInfo probeInfo, IBlockState blockState, Block block, World world,
                                             BlockPos pos, EntityPlayer player, IProbeHitData data) {
        String modid = Tools.getModName(block);

        ItemStack pickBlock = data.getPickBlock();

        if (block instanceof BlockSilverfish && mode != ProbeMode.DEBUG && !Tools.show(mode,config.getShowSilverfish())) {
            BlockSilverfish.EnumType type = blockState.getValue(BlockSilverfish.VARIANT);
            blockState = type.getModelBlock();
            block = blockState.getBlock();
            pickBlock = new ItemStack(block, 1, block.getMetaFromState(blockState));
        }

        if (block instanceof BlockFluidBase || block instanceof BlockLiquid) {
            Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
            if (fluid != null) {
                FluidStack fluidStack = new FluidStack(fluid, Fluid.BUCKET_VOLUME);
                ItemStack bucketStack = FluidUtil.getFilledBucket(fluidStack);

                IProbeInfo horizontal = probeInfo.horizontal();
                if (fluidStack.isFluidEqual(FluidUtil.getFluidContained(bucketStack))) {
                    horizontal.item(bucketStack);
                } else {
                    horizontal.icon(fluid.getStill(), -1, -1, 16, 16, probeInfo.defaultIconStyle().width(20));
                }

                horizontal.vertical()
                        .text(NAME + fluidStack.getLocalizedName())
                        .text(MODNAME + modid);
                return;
            }
        }

        if (!pickBlock.isEmpty()) {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.horizontal()
                        .item(pickBlock)
                        .vertical()
                        .itemLabel(pickBlock)
                        .text(MODNAME + modid);
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(pickBlock)
                        .itemLabel(pickBlock);
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
}
