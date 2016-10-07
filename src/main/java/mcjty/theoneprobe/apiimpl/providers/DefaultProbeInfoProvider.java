package mcjty.theoneprobe.apiimpl.providers;

import cofh.api.energy.IEnergyHandler;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (Tools.show(mode, config.getShowHarvestLevel())) {
            showHarvestLevel(probeInfo, blockState, block);
        }
        if (Tools.show(mode, config.getShowCanBeHarvested())) {
            showCanBeHarvested(probeInfo, world, pos, block, player);
        }
        if (Tools.show(mode, config.getShowRedstone())) {
            showRedstonePower(probeInfo, world, blockState, data, block, Tools.show(mode, config.getShowLeverSetting()));
        }
        if (Tools.show(mode, config.getShowLeverSetting())) {
            showLeverSetting(probeInfo, world, blockState, data, block);
        }
        if (Tools.show(mode, config.getShowChestContents())) {
            showChestContents(probeInfo, world, pos);
        }

        if (config.getRFMode() > 0) {
            showRF(probeInfo, world, pos);
        }
        if (config.getTankMode() > 0) {
            showTankInfo(probeInfo, world, pos);
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
            probeInfo.horizontal().item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14)).text("Power: " + redstonePower);
        }
    }

    private void showLeverSetting(IProbeInfo probeInfo, World world, IBlockState blockState, IProbeHitData data, Block block) {
        if (block instanceof BlockLever) {
            Boolean powered = blockState.getValue(BlockLever.POWERED);
            probeInfo.horizontal().item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14))
                    .text("State: " + (powered ? "On" : "Off"));
        }
    }

    private void showTankInfo(IProbeInfo probeInfo, World world, BlockPos pos) {
        ProbeConfig config = Config.getDefaultConfig();
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            net.minecraftforge.fluids.capability.IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if (handler != null) {
                IFluidTankProperties[] properties = handler.getTankProperties();
                if (properties != null && properties.length > 0) {
                    FluidStack fluidStack = properties[0].getContents();
                    int maxContents = properties[0].getCapacity();
                    addFluidInfo(probeInfo, config, fluidStack, maxContents);
                }
            }
        } else if (te instanceof IFluidHandler) {
            IFluidHandler handler = (IFluidHandler) te;
            FluidTankInfo[] info = handler.getTankInfo(null);
            if (info != null && info.length > 0) {
                FluidStack fluidStack = info[0].fluid;
                int maxContents = info[0].capacity;
                addFluidInfo(probeInfo, config, fluidStack, maxContents);
            }
        }
    }

    private void addFluidInfo(IProbeInfo probeInfo, ProbeConfig config, FluidStack fluidStack, int maxContents) {
        int contents = fluidStack == null ? 0 : fluidStack.amount;
        if (fluidStack != null) {
            probeInfo.text("Liquid: " + fluidStack.getLocalizedName());
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
            probeInfo.text(TextFormatting.GREEN + ElementProgress.format(contents, Config.tankFormat, "mB"));
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
                probeInfo.text(TextFormatting.GREEN + "RF: " + ElementProgress.format(energy, Config.rfFormat, "RF"));
            }
        }
    }

    private String[] harvestLevels = new String[] {
            "stone",
            "iron",
            "diamond",
            "obsidian",
            "cobalt"
    };

    private void showHarvestLevel(IProbeInfo probeInfo, IBlockState blockState, Block block) {
        String harvestTool = block.getHarvestTool(blockState);
        if (harvestTool != null) {
            int harvestLevel = block.getHarvestLevel(blockState);
            String harvestName;
            if (harvestLevel >= harvestLevels.length) {
                harvestName = Integer.toString(harvestLevel);
            } else if (harvestLevel < 0) {
                harvestName = Integer.toString(harvestLevel);
            } else {
                harvestName = harvestLevels[harvestLevel];
            }
            probeInfo.text(TextFormatting.GREEN + "Tool: " + harvestTool + " (level " + harvestName + ")");
        }
    }

    private void showCanBeHarvested(IProbeInfo probeInfo, World world, BlockPos pos, Block block, EntityPlayer player) {
        if (ModItems.isProbe(player.getHeldItemMainhand())) {
            // If the player holds the probe there is no need to show harvestability information as the
            // probe cannot harvest anything. This is only supposed to work in off hand.
            return;
        }

        boolean harvestable = block.canHarvestBlock(world, pos, player) && world.getBlockState(pos).getBlockHardness(world, pos) >= 0;
        if (harvestable) {
            probeInfo.text(TextFormatting.GREEN + "Harvestable");
        } else {
            probeInfo.text(TextFormatting.YELLOW + "Not harvestable");
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
                            .text(TextFormatting.WHITE + stack.getLocalizedName())
                            .text(TextFormatting.BLUE + modid);
                return;
            }
        }

        ItemStack pickBlock = data.getPickBlock();
        if (pickBlock != null) {
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

    private void addItemStack(List<ItemStack> stacks, Set<Item> foundItems, ItemStack stack) {
        if (stack == null) {
            return;
        }
        if (foundItems != null && foundItems.contains(stack.getItem())) {
            for (ItemStack s : stacks) {
                if (ItemHandlerHelper.canItemStacksStack(s, stack)) {
                    s.stackSize += stack.stackSize;
                    return;
                }
            }
        }
        // If we come here we need to append a new stack
        stacks.add(stack.copy());
        if (foundItems != null) {
            foundItems.add(stack.getItem());
        }
    }

    private void showChestContents(IProbeInfo probeInfo, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        IProbeInfo vertical = null;
        IProbeInfo horizontal = null;

        Set<Item> foundItems = Config.compactEqualStacks ? new HashSet<>() : null;
        List<ItemStack> stacks = new ArrayList<>();

        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler capability = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = 0 ; i < capability.getSlots() ; i++) {
                addItemStack(stacks, foundItems, capability.getStackInSlot(i));
            }
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            for (int i = 0 ; i < inventory.getSizeInventory() ; i++) {
                addItemStack(stacks, foundItems, inventory.getStackInSlot(i));
            }
        }

        int rows = 0;
        int idx = 0;

        for (ItemStack stackInSlot : stacks) {
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

}
