package mcjty.theoneprobe.apiimpl.providers;

import mcjty.lib.api.power.IBigPower;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.compat.TeslaTools;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.ComparatorMode;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.Collections;
import net.minecraftforge.registries.ForgeRegistries;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;
import static mcjty.theoneprobe.api.TextStyleClass.*;
import static net.minecraftforge.fluids.FluidAttributes.BUCKET_VOLUME;

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
            TileEntity te = world.getTileEntity(data.getPos());
            if (te instanceof BrewingStandTileEntity) {
                int brewtime = ((BrewingStandTileEntity) te).brewTime;
                int fuel = ((BrewingStandTileEntity) te).fuel;
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
        if (block instanceof SpawnerBlock) {
            TileEntity te = world.getTileEntity(data.getPos());
            if (te instanceof MobSpawnerTileEntity) {
                AbstractSpawner logic = ((MobSpawnerTileEntity) te).getSpawnerBaseLogic();
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(logic.getEntityId());
                if (type != null) {
                    String mobName = type.getName().getFormattedText();
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle()
                          .alignment(ElementAlignment.ALIGN_CENTER))
                          .text(LABEL + "Mob: " + INFO + mobName);
                }
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
            redstonePower = world.getRedstonePower(data.getPos(), data.getSideHit().getOpposite());
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
            ComparatorMode mode = blockState.get(ComparatorBlock.MODE);
            probeInfo.text(LABEL + "Mode: " + INFO + mode.getName());
        } else if (block instanceof RepeaterBlock) {
            Boolean locked = blockState.get(RepeaterBlock.LOCKED);
            Integer delay = blockState.get(RepeaterBlock.DELAY);
            probeInfo.text(LABEL + "Delay: " + INFO + delay + " ticks");
            if (locked) {
                probeInfo.text(INFO + "Locked");
            }
        }
    }

    private void showTankInfo(IProbeInfo probeInfo, World world, BlockPos pos) {
        ProbeConfig config = Config.getDefaultConfig();
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent()) {
            te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(handler -> {
                for (int i = 0 ; i < handler.getTanks() ; i++) {
                    FluidStack fluidStack = handler.getFluidInTank(i);
                    int maxContents = handler.getTankCapacity(i);
                    if (!fluidStack.isEmpty()) {
                        addFluidInfo(probeInfo, config, fluidStack, maxContents);
                    }
                }
            });
        }
    }

    private void addFluidInfo(IProbeInfo probeInfo, ProbeConfig config, FluidStack fluidStack, int maxContents) {
        int contents = fluidStack.getAmount();
        if (!fluidStack.isEmpty()) {
            probeInfo.text(NAME + "Liquid:" + STARTLOC + fluidStack.getTranslationKey() + ENDLOC);
        }
        if (config.getTankMode() == 1) {
            probeInfo.progress(contents, maxContents,
                    probeInfo.defaultProgressStyle()
                            .suffix("mB")
                            .filledColor(Config.tankbarFilledColor)
                            .alternateFilledColor(Config.tankbarAlternateFilledColor)
                            .borderColor(Config.tankbarBorderColor)
                            .numberFormat(Config.tankFormat.get()));
        } else {
            probeInfo.text(PROGRESS + ElementProgress.format(contents, Config.tankFormat.get(), "mB"));
        }
    }

    private void showRF(IProbeInfo probeInfo, World world, BlockPos pos) {
        ProbeConfig config = Config.getDefaultConfig();
        TileEntity te = world.getTileEntity(pos);
        if (TheOneProbe.tesla && TeslaTools.isEnergyHandler(te)) {
            long energy = TeslaTools.getEnergy(te);
            long maxEnergy = TeslaTools.getMaxEnergy(te);
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (te instanceof IBigPower) {
            long energy = ((IBigPower) te).getStoredPower();
            long maxEnergy = ((IBigPower) te).getCapacity();
            addRFInfo(probeInfo, config, energy, maxEnergy);
        } else if (te != null && te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            te.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                addRFInfo(probeInfo, config, handler.getEnergyStored(), handler.getMaxEnergyStored());
            });
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
                            .numberFormat(Config.rfFormat.get()));
        } else {
            probeInfo.text(PROGRESS + "RF: " + ElementProgress.format(energy, Config.rfFormat.get(), "RF"));
        }
    }

    private void showGrowthLevel(IProbeInfo probeInfo, BlockState blockState) {
        for (IProperty<?> property : blockState.getProperties()) {
            if (!"age".equals(property.getName())) {
                continue;
            }
            if(property.getValueClass() == Integer.class) {
                IProperty<Integer> integerProperty = (IProperty<Integer>)property;
                int age = blockState.get(integerProperty);
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

    public static void showStandardBlockInfo(IProbeConfig config, ProbeMode mode, IProbeInfo probeInfo, BlockState blockState, Block block, World world,
                                             BlockPos pos, PlayerEntity player, IProbeHitData data) {
        String modid = Tools.getModName(block);

        ItemStack pickBlock = data.getPickBlock();

        if (block instanceof SilverfishBlock && mode != ProbeMode.DEBUG && !Tools.show(mode,config.getShowSilverfish())) {
            block = ((SilverfishBlock) block).getMimickedBlock();
            pickBlock = new ItemStack(block, 1);
        }

        if (block instanceof FlowingFluidBlock) {
            IFluidState fluidState = block.getFluidState(blockState);
            Fluid fluid = fluidState.getFluid();
            if (fluid != Fluids.EMPTY) {
                FluidStack fluidStack = new FluidStack(fluid.getFluid(), BUCKET_VOLUME);
                ItemStack bucketStack = FluidUtil.getFilledBucket(fluidStack);

                IProbeInfo horizontal = probeInfo.horizontal();
                FluidUtil.getFluidContained(bucketStack).ifPresent(fc -> {
                    if (fluidStack.isFluidEqual(fc)) {
                        horizontal.item(bucketStack);
                    } else {
                        horizontal.icon(fluid.getAttributes().getStillTexture(), -1, -1, 16, 16, probeInfo.defaultIconStyle().width(20));
                    }
                });

                horizontal.vertical()
                        .text(NAME + STARTLOC + fluidStack.getTranslationKey() + ENDLOC)
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
