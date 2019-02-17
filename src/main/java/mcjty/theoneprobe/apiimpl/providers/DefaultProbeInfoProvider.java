package mcjty.theoneprobe.apiimpl.providers;

import mcjty.lib.api.power.IBigPower;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.compat.RedstoneFluxTools;
import mcjty.theoneprobe.compat.TeslaTools;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sortme.MobSpawnerLogic;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;
import static mcjty.theoneprobe.api.TextStyleClass.*;

public class DefaultProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return TheOneProbe.MODID + ":default";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        Block block = blockState.getBlock();
        BlockPos pos = data.getPos();

        IProbeConfig config = Config.getRealConfig();

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
        if (block instanceof BrewingStandBlock) {
            BlockEntity te = world.getBlockEntity(data.getPos());
            // @todo fabric
//            if (te instanceof BrewingStandBlockEntity) {
//                int brewtime = ((BrewingStandBlockEntity) te).getInvProperty(0);
//                int fuel = ((BrewingStandBlockEntity) te).getInvProperty(1);
//                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
//                        .item(new ItemStack(Items.BLAZE_POWDER), probeInfo.defaultItemStyle().width(16).height(16))
//                        .text(LABEL + "Fuel: " + INFO + fuel);
//                if (brewtime > 0) {
//                    probeInfo.text(LABEL + "Time: " + INFO + brewtime + " ticks");
//                }
//
//            }
        }
    }

    private void showMobSpawnerInfo(IProbeInfo probeInfo, World world, IProbeHitData data, Block block) {
        if (block instanceof MobSpawnerBlock) {
            BlockEntity te = world.getBlockEntity(data.getPos());
            if (te instanceof MobSpawnerBlockEntity) {
                MobSpawnerLogic logic = ((MobSpawnerBlockEntity) te).getLogic();
                // @todo fabric
//                String mobName = logic.getCachedEntity().getName();
//                probeInfo.horizontal(probeInfo.defaultLayoutStyle()
//                    .alignment(ElementAlignment.ALIGN_CENTER))
//                    .text(LABEL + "Mob: " + INFO + mobName);
            }
        }
    }

    private void showRedstonePower(IProbeInfo probeInfo, World world, BlockState blockState, IProbeHitData data, Block block,
                                   boolean showLever) {
        if (showLever && block instanceof LeverBlock) {
            // We are showing the lever setting so we don't show redstone in that case
            return;
        }
        int redstonePower;
        if (block instanceof RedstoneWireBlock) {
            redstonePower = blockState.get(RedstoneWireBlock.POWER);
        } else {
            redstonePower = world.getEmittedRedstonePower(data.getPos(), data.getSideHit().getOpposite());
        }
        if (redstonePower > 0) {
            probeInfo.horizontal()
                    .item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text(LABEL + "Power: " + INFO + redstonePower);
        }
    }

    private void showLeverSetting(IProbeInfo probeInfo, World world, BlockState blockState, IProbeHitData data, Block block) {
        if (block instanceof LeverBlock) {
            Boolean powered = blockState.get(LeverBlock.POWERED);
            probeInfo.horizontal().item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text(LABEL + "State: " + INFO + (powered ? "On" : "Off"));
        } else if (block instanceof ComparatorBlock) {
            ComparatorMode mode = blockState.get(ComparatorBlock.field_10789);
            probeInfo.text(LABEL + "Mode: " + INFO + mode.name());
        } else if (block instanceof RepeaterBlock) {
            Boolean locked = blockState.get(RepeaterBlock.LOCKED);
            Integer delay = blockState.get(RepeaterBlock.field_11451);
            probeInfo.text(LABEL + "Delay: " + INFO + delay + " ticks");
            if (locked) {
                probeInfo.text(INFO + "Locked");
            }
        }
    }

    // @todo fabric
    private void showTankInfo(IProbeInfo probeInfo, World world, BlockPos pos) {
        // @todo config
//        ProbeConfig config = Config.getDefaultConfig();
//        BlockEntity te = world.getBlockEntity(pos);
//        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
//            net.minecraftforge.fluids.capability.IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
//            if (handler != null) {
//                IFluidTankProperties[] properties = handler.getTankProperties();
//                if (properties != null) {
//                    for (IFluidTankProperties property : properties) {
//                        if (property != null) {
//                            FluidStack fluidStack = property.getContents();
//                            int maxContents = property.getCapacity();
//                            addFluidInfo(probeInfo, config, fluidStack, maxContents);
//                        }
//                    }
//                }
//            }
//        }
    }

//    private void addFluidInfo(IProbeInfo probeInfo, ProbeConfig config, FluidStack fluidStack, int maxContents) {
//        int contents = fluidStack == null ? 0 : fluidStack.amount;
//        if (fluidStack != null) {
//            probeInfo.text(NAME + "Liquid: " + fluidStack.getLocalizedName());
//        }
//        if (config.getTankMode() == 1) {
//            probeInfo.progress(contents, maxContents,
//                    probeInfo.defaultProgressStyle()
//                            .suffix("mB")
//                            .filledColor(Config.tankbarFilledColor)
//                            .alternateFilledColor(Config.tankbarAlternateFilledColor)
//                            .borderColor(Config.tankbarBorderColor)
//                            .numberFormat(Config.tankFormat));
//        } else {
//            probeInfo.text(PROGRESS + ElementProgress.format(contents, Config.tankFormat, "mB"));
//        }
//    }

    private void showRF(IProbeInfo probeInfo, World world, BlockPos pos) {
        ProbeConfig config = Config.getDefaultConfig();
        BlockEntity te = world.getBlockEntity(pos);
        if (TheOneProbe.tesla && TeslaTools.isEnergyHandler(te)) {
            long energy = TeslaTools.getEnergy(te);
            long maxEnergy = TeslaTools.getMaxEnergy(te);
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (te instanceof IBigPower) {
            long energy = ((IBigPower) te).getStoredPower();
            long maxEnergy = ((IBigPower) te).getCapacity();
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (TheOneProbe.redstoneflux && RedstoneFluxTools.isEnergyHandler(te)) {
            int energy = RedstoneFluxTools.getEnergy(te);
            int maxEnergy = RedstoneFluxTools.getMaxEnergy(te);
            addRFInfo(probeInfo, config, energy, maxEnergy);
            // @todo fabric
//        } else if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, null)) {
//            net.minecraftforge.energy.IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, null);
//            if (handler != null) {
//                addRFInfo(probeInfo, config, handler.getEnergyStored(), handler.getMaxEnergyStored());
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

    private void showGrowthLevel(IProbeInfo probeInfo, BlockState blockState) {
        for (Property<?> property : blockState.getProperties()) {
            if(!"age".equals(property.getName())) continue;
            if(property.getValueClass() == Integer.class) {
                Property<Integer> integerProperty = (Property<Integer>)property;
                int age = blockState.get(integerProperty);
                int maxAge = Collections.max(integerProperty.getValues());
                if (age == maxAge) {
                    probeInfo.text(OK + "Fully grown");
                } else {
                    probeInfo.text(LABEL + "Growth: " + WARNING + (age * 100) / maxAge + "%");
                }
            }
            return;
        }
    }

    public static void showStandardBlockInfo(IProbeConfig config, ProbeMode mode, IProbeInfo probeInfo, BlockState blockState, Block block, World world,
                                             BlockPos pos, PlayerEntity player, IProbeHitData data) {
        String modid = Tools.getModName(block);

        ItemStack pickBlock = data.getPickBlock();

        if (block instanceof InfestedBlock && mode != ProbeMode.DEBUG && !Tools.show(mode,config.getShowSilverfish())) {
            block = ((InfestedBlock) block).getRegularBlock();
            pickBlock = new ItemStack(block, 1);
        }

        if (block instanceof FluidBlock) {
            FluidState fluidState = world.getFluidState(pos);
            if (fluidState != null) {
                Fluid fluid = fluidState.getFluid();
                Item bucketItem = fluid.getBucketItem();

                IProbeInfo horizontal = probeInfo.horizontal();
                if (bucketItem != Items.AIR) {
                    horizontal.item(new ItemStack(bucketItem));
//                } else {
//                    horizontal.icon(fluid.getStill(), -1, -1, 16, 16, probeInfo.defaultIconStyle().width(20));
                }

                horizontal.vertical()
                        .text(NAME + STARTLOC + block.getTranslationKey() + ENDLOC)
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
        return STARTLOC + block.getTranslationKey() + ".name" + ENDLOC;
    }
}
