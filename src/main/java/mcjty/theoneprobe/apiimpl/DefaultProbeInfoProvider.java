package mcjty.theoneprobe.apiimpl;

import java.cofh.api.energy.IEnergyHandler;
import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.ProgressStyle;
import mcjty.theoneprobe.apiimpl.elements.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class DefaultProbeInfoProvider implements IProbeInfoProvider {

    public static int ELEMENT_TEXT;
    public static int ELEMENT_ITEM;
    public static int ELEMENT_PROGRESS;
    public static int ELEMENT_HORIZONTAL;
    public static int ELEMENT_VERTICAL;

    public DefaultProbeInfoProvider() {
        ELEMENT_TEXT = TheOneProbe.theOneProbeImp.registerElementFactory(ElementText::new);
        ELEMENT_ITEM = TheOneProbe.theOneProbeImp.registerElementFactory(ElementItemStack::new);
        ELEMENT_PROGRESS = TheOneProbe.theOneProbeImp.registerElementFactory(ElementProgress::new);
        ELEMENT_HORIZONTAL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementHorizontal::new);
        ELEMENT_VERTICAL = TheOneProbe.theOneProbeImp.registerElementFactory(ElementVertical::new);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, World world, IBlockState blockState, BlockPos pos) {
        Block block = blockState.getBlock();

        showStandardBlockInfo(probeInfo, block);

        if (mode == ProbeMode.EXTENDED) {
            if (Config.showHarvestLevel) {
                showHarvestLevel(probeInfo, blockState, block);
            }

            if (Config.showChestContents) {
                showChestContents(probeInfo, world, pos);
            }
        }

        if (mode == ProbeMode.DEBUG && Config.showDebugInfo) {
            showDebugInfo(probeInfo, world, blockState, pos, block);
        }

        if (Config.showRF > 0) {
            showRF(probeInfo, world, pos);
        }
    }

    private void showRF(IProbeInfo probeInfo, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEnergyHandler) {
            IEnergyHandler handler = (IEnergyHandler) te;
            int energy = handler.getEnergyStored(EnumFacing.DOWN);
            int maxEnergy = handler.getMaxEnergyStored(EnumFacing.DOWN);
            if (Config.showRF == 1) {
                probeInfo.progress(energy, maxEnergy, "", "RF",
                        new ProgressStyle().filledColor(0xffdd0000).alternateFilledColor(0xff430000).borderColor(0xff555555).numberFormat(Config.rfFormat));
            } else {
                probeInfo.text(TextFormatting.GREEN + "RF: " + ElementProgress.format(energy, Config.rfFormat) + "RF");
            }
        }
    }

    private void showDebugInfo(IProbeInfo probeInfo, World world, IBlockState blockState, BlockPos pos, Block block) {
        IProbeInfo vertical = probeInfo.vertical(0xffff4444, 2)
                .text("Unlocname: " + block.getUnlocalizedName())
                .text("Meta: " + blockState.getBlock().getMetaFromState(blockState));
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            vertical.text("TE: " + te.getClass().getSimpleName());
        }
    }

    private void showHarvestLevel(IProbeInfo probeInfo, IBlockState blockState, Block block) {
        String harvestTool = block.getHarvestTool(blockState);
        if (harvestTool != null) {
            probeInfo.text(TextFormatting.GREEN + "Harvest level: " + harvestTool);
        }
    }

    private void showStandardBlockInfo(IProbeInfo probeInfo, Block block) {
        String modid = getModName(block);
        Item item = Item.getItemFromBlock(block);
        if (item != null) {
            ItemStack stack = new ItemStack(item, 1);
            probeInfo.horizontal()
                    .item(stack)
                    .vertical()
                        .text(TextFormatting.WHITE + stack.getDisplayName())
                        .text(TextFormatting.BLUE + modid);
        } else {
            probeInfo.horizontal()
                    .text(TextFormatting.WHITE + block.getLocalizedName())
                    .text(TextFormatting.BLUE + modid);
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
                            vertical = probeInfo.vertical(0xffffffff, 0);
                        }
                        horizontal = vertical.horizontal(null, 0);
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
                            vertical = probeInfo.vertical(0xffffffff, 0);
                        }
                        horizontal = vertical.horizontal();
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

    private String getModName(Block block) {
        String modid = block.getRegistryName().getResourceDomain();
        for (ModContainer container : Loader.instance().getActiveModList()) {
            if (modid.equals(container.getModId())) {
                modid = container.getName();
                break;
            }
        }
        return modid;
    }
}
