package mcjty.theoneprobe.apiimpl.providers;

import com.mojang.authlib.GameProfile;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.compat.TeslaTools;
import mcjty.theoneprobe.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static mcjty.theoneprobe.api.TextStyleClass.*;
import static net.neoforged.neoforge.fluids.FluidType.BUCKET_VOLUME;

public class DefaultProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(TheOneProbe.MODID, "default");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
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
            HarvestInfoTools.showHarvestLevel(probeInfo, blockState);
        } else if (showHarvested) {
            HarvestInfoTools.showCanBeHarvested(probeInfo, world, pos, blockState, player);
        }

        if (Tools.show(mode, config.getShowRedstone())) {
            showRedstonePower(probeInfo, world, blockState, data, block, Tools.show(mode, config.getShowLeverSetting()));
        }
        if (Tools.show(mode, config.getShowLeverSetting())) {
            showLeverSetting(probeInfo, world, blockState, data, block);
        }

        ChestInfoTools.showChestInfo(mode, probeInfo, world, pos, config);

        if (config.getRFMode() > 0) {
            showEnergy(probeInfo, world, pos);
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

        if (Tools.show(mode, config.getShowNoteblockInfo())) {
            showNoteblockInfo(probeInfo, world, data, blockState);
        }

        if (Tools.show(mode, config.getShowSkullInfo())) {
            showSkullInfo(probeInfo, world, data, blockState);
        }
    }

    private void showBrewingStandInfo(IProbeInfo probeInfo, Level world, IProbeHitData data, Block block) {
        if (block instanceof BrewingStandBlock) {
            BlockEntity te = world.getBlockEntity(data.getPos());
            if (te instanceof BrewingStandBlockEntity brewingStand) {
                int brewtime = brewingStand.brewTime;
                int fuel = brewingStand.fuel;
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(new ItemStack(Items.BLAZE_POWDER), probeInfo.defaultItemStyle().width(16).height(16))
                        .text(CompoundText.createLabelInfo("Fuel: ", fuel));
                if (brewtime > 0) {
                    probeInfo.text(CompoundText.createLabelInfo("Time: ", brewtime + " ticks"));
                }

            }
        }
    }

    private static final String[] NOTE_TABLE = {
            "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#"
    };

    private void showNoteblockInfo(IProbeInfo probeInfo, Level world, IProbeHitData data, BlockState blockState) {
        if (blockState.getBlock() instanceof NoteBlock) {
            int note = blockState.getValue(NoteBlock.NOTE);
            NoteBlockInstrument instrument = blockState.getValue(NoteBlock.INSTRUMENT);
            if (note < 0) {
                note = 0;
            } else if (note > 24) {
                note = 24;
            }
            probeInfo.horizontal(probeInfo.defaultLayoutStyle()
                    .alignment(ElementAlignment.ALIGN_CENTER))
                    .text(CompoundText.create().style(LABEL).text("Note: ")
                            .info(instrument.name().toLowerCase() + " " + NOTE_TABLE[note] + " (" + note + ")"));
        }
    }

    private void showSkullInfo(IProbeInfo probeInfo, Level world, IProbeHitData data, BlockState blockState) {
        if (blockState.getBlock() instanceof SkullBlock) {
            BlockEntity te = world.getBlockEntity(data.getPos());
            if (te instanceof SkullBlockEntity skullBlockEntity) {
                ResolvableProfile profile = skullBlockEntity.getOwnerProfile();
                if (profile != null) {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle()
                            .alignment(ElementAlignment.ALIGN_CENTER))
                            .text(CompoundText.create().style(LABEL).text("Player: ")
                                    .info(profile.name().orElse("???")));
                }
            }
        }
    }

    private void showMobSpawnerInfo(IProbeInfo probeInfo, Level world, IProbeHitData data, Block block) {
        if (block instanceof SpawnerBlock) {
            BlockEntity te = world.getBlockEntity(data.getPos());
            if (te instanceof SpawnerBlockEntity spawnerBlock) {
                BaseSpawner logic = spawnerBlock.getSpawner();
                CompoundTag tag = logic.nextSpawnData.getEntityToSpawn();
                Optional<EntityType<?>> optional = EntityType.by(tag);
                optional.ifPresent(type -> probeInfo.horizontal(probeInfo.defaultLayoutStyle()
                        .alignment(ElementAlignment.ALIGN_CENTER))
                        .text(CompoundText.create().style(LABEL).text("Mob: ").info(type.getDescriptionId())));
            }
        }
    }

    private void showRedstonePower(IProbeInfo probeInfo, Level world, BlockState blockState, IProbeHitData data, Block block,
								   boolean showLever) {
        if (showLever && block instanceof LeverBlock) {
            // We are showing the lever setting so we don't show redstone in that case
            return;
        }
        int redstonePower;
        if (block instanceof RedStoneWireBlock) {
            redstonePower = blockState.getValue(RedStoneWireBlock.POWER);
        } else {
            redstonePower = world.getSignal(data.getPos(), data.getSideHit().getOpposite());
        }
        if (redstonePower > 0) {
            probeInfo.horizontal()
                    .item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text(CompoundText.createLabelInfo("Power: ", redstonePower));
        }
    }

    private void showLeverSetting(IProbeInfo probeInfo, Level world, BlockState blockState, IProbeHitData data, Block block) {
        if (block instanceof LeverBlock) {
            Boolean powered = blockState.getValue(LeverBlock.POWERED);
            probeInfo.horizontal().item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text(CompoundText.createLabelInfo("State: ", (powered ? "On" : "Off")));
        } else if (block instanceof ComparatorBlock) {
            ComparatorMode mode = blockState.getValue(ComparatorBlock.MODE);
            probeInfo.text(CompoundText.createLabelInfo("Mode: ", mode.getSerializedName()));
        } else if (block instanceof RepeaterBlock) {
            Boolean locked = blockState.getValue(RepeaterBlock.LOCKED);
            Integer delay = blockState.getValue(RepeaterBlock.DELAY);
            probeInfo.text(CompoundText.createLabelInfo("Delay: ", delay + " ticks"));
            if (locked) {
                probeInfo.text(CompoundText.create().style(INFO).text("Locked"));
            }
        }
    }

    private void showTankInfo(IProbeInfo probeInfo, Level world, BlockPos pos) {
        ProbeConfig config = Config.getDefaultConfig();
        BlockEntity te = world.getBlockEntity(pos);
        IFluidHandler handler = world.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
        if (handler != null) {
            for (int i = 0; i < handler.getTanks(); i++) {
                FluidStack fluidStack = handler.getFluidInTank(i);
                int maxContents = handler.getTankCapacity(i);
                if (!fluidStack.isEmpty()) {
                    addFluidInfo(probeInfo, config, fluidStack, maxContents);
                }
            }
        }
    }

    private void addFluidInfo(IProbeInfo probeInfo, ProbeConfig config, FluidStack fluidStack, int maxContents) {
        int contents = fluidStack.getAmount();
    	if(config.getTankMode() == 1) {

            int tintColor = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
            Color color = new Color(tintColor);
        	if (Objects.equals(fluidStack.getFluid(), Fluids.LAVA)) {
    			color = new Color(255, 139, 27);
        	}
        	MutableComponent text = Component.literal("");
        	text.append(ElementProgress.format(contents, Config.tankFormat.get(), Component.literal("mB")));
        	text.append("/");
        	text.append(ElementProgress.format(maxContents, Config.tankFormat.get(), Component.literal("mB")));
        	probeInfo.tankSimple(maxContents, fluidStack, 
        			probeInfo.defaultProgressStyle()
        			.numberFormat(NumberFormat.NONE)
        			.borderlessColor(color, color.darker().darker())
        			.prefix(((MutableComponent)fluidStack.getDisplayName()).append(": "))
        			.suffix(text));
        } else {
            if (!fluidStack.isEmpty()) {
                probeInfo.text(CompoundText.create().style(NAME).text("Liquid:").info(fluidStack.getTranslationKey()));
            }
            if (config.getTankMode() == 2) {
                probeInfo.progress(contents, maxContents,
                        probeInfo.defaultProgressStyle()
                                .suffix("mB")
                                .filledColor(Config.tankbarFilledColor)
                                .alternateFilledColor(Config.tankbarAlternateFilledColor)
                                .borderColor(Config.tankbarBorderColor)
                                .numberFormat(Config.tankFormat.get()));
            } else {
                probeInfo.text(CompoundText.create().style(PROGRESS).text(ElementProgress.format(contents, Config.tankFormat.get(), Component.literal("mB"))));
            }
        }
    }

    private void showEnergy(IProbeInfo probeInfo, Level world, BlockPos pos) {
        ProbeConfig config = Config.getDefaultConfig();
        BlockEntity te = world.getBlockEntity(pos);
        if (TheOneProbe.tesla && TeslaTools.isEnergyHandler(te)) {
            long energy = TeslaTools.getEnergy(te);
            long maxEnergy = TeslaTools.getMaxEnergy(te);
            addEnergyInfo(probeInfo, config, energy, maxEnergy);
//        } else if (te instanceof IBigPower bigPower) {
//            long energy = bigPower.getStoredPower();
//            long maxEnergy = bigPower.getCapacity();
//            addEnergyInfo(probeInfo, config, energy, maxEnergy);
        } else{
            IEnergyStorage handler = world.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
            if (handler != null) {
                addEnergyInfo(probeInfo, config, handler.getEnergyStored(), handler.getMaxEnergyStored());
            }
        }
    }

    private void addEnergyInfo(IProbeInfo probeInfo, ProbeConfig config, long energy, long maxEnergy) {
        if (config.getRFMode() == 1) {
            probeInfo.progress(energy, maxEnergy,
                    probeInfo.defaultProgressStyle()
                            .suffix("FE")
                            .filledColor(Config.rfbarFilledColor)
                            .alternateFilledColor(Config.rfbarAlternateFilledColor)
                            .borderColor(Config.rfbarBorderColor)
                            .numberFormat(Config.rfFormat.get()));
        } else {
            probeInfo.text(CompoundText.create().style(PROGRESS).text("FE: " + ElementProgress.format(energy, Config.rfFormat.get(), Component.literal("FE"))));
        }
    }

    private void showGrowthLevel(IProbeInfo probeInfo, BlockState blockState) {
        for (Property<?> property : blockState.getProperties()) {
            if (!"age".equals(property.getName())) {
                continue;
            }
            if (property.getValueClass() == Integer.class) {
                @SuppressWarnings("unchecked")
                Property<Integer> integerProperty = (Property<Integer>) property;
                int age = blockState.getValue(integerProperty);
                int maxAge = Collections.max(integerProperty.getPossibleValues());
                if (age == maxAge) {
                    probeInfo.text(CompoundText.create().style(OK).text("Fully grown"));
                } else {
                    probeInfo.text(CompoundText.create().style(LABEL).text("Growth: ").style(WARNING).text((age * 100) / maxAge + "%"));
                }
            }
            return;
        }
    }

    public static void showStandardBlockInfo(IProbeConfig config, ProbeMode mode, IProbeInfo probeInfo, BlockState blockState, Block block, Level world,
											 BlockPos pos, Player player, IProbeHitData data) {
        String modName = Tools.getModName(block);

        ItemStack pickBlock = data.getPickBlock();

        if (block instanceof InfestedBlock && mode != ProbeMode.DEBUG && !Tools.show(mode, config.getShowSilverfish())) {
            block = ((InfestedBlock) block).getHostBlock();
            pickBlock = new ItemStack(block, 1);
        }

        if (block instanceof LiquidBlock) {
            FluidState fluidState = blockState.getFluidState();
            Fluid fluid = fluidState.getType();
            if (!Objects.equals(fluid, Fluids.EMPTY)) {
                IProbeInfo horizontal = probeInfo.horizontal();
                FluidStack fluidStack = new FluidStack(fluid, BUCKET_VOLUME);

                int tintColor = IClientFluidTypeExtensions.of(fluid).getTintColor(fluidStack);
                ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluid).getStillTexture();
                Color color = new Color(tintColor);
                horizontal.icon(stillTexture, -1, -1, 16, 16,
                        probeInfo.defaultIconStyle().width(20).color(color));
                //Proposal Fluids should look at the icon only not buckets of it. Dunno you have to decide. I just fixed the fluid color bug
                ItemStack bucketStack = FluidUtil.getFilledBucket(fluidStack);
                FluidUtil.getFluidContained(bucketStack).ifPresent(fc -> {
                    if (fluidStack.isFluidEqual(fc)) {
                        horizontal.item(bucketStack);
                    }
                });

                horizontal.vertical()
                        .text(CompoundText.create().name(fluidStack.getTranslationKey()))
                        .text(CompoundText.create().style(MODNAME).text(modName));
                return;
            }
        }

        if (!pickBlock.isEmpty()) {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.horizontal()
                        .item(pickBlock)
                        .vertical()
                        .itemLabel(pickBlock)
                        .text(CompoundText.create().style(MODNAME).text(modName));
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(pickBlock)
                        .itemLabel(pickBlock);
            }
        } else {
            if (Tools.show(mode, config.getShowModName())) {
                probeInfo.vertical()
                        .text(CompoundText.create().name(block.getDescriptionId()))//This should maybe send over the registry name and convert it into the block name on the client
                        .text(CompoundText.create().style(MODNAME).text(modName));
            } else {
                probeInfo.vertical()
                        .text(CompoundText.create().name(block.getDescriptionId()));
            }
        }
    }
}
