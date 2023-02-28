package mcjty.theoneprobe.apiimpl.providers;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ChestInfoTools {

    static void showChestInfo(ProbeMode mode, IProbeInfo probeInfo, Level world, BlockPos pos, IProbeConfig config) {
        ResourceLocation registryKey = ForgeRegistries.BLOCKS.getKey(world.getBlockState(pos).getBlock());
        BlockEntity te = world.getBlockEntity(pos);
        showChestInfo(mode, probeInfo, registryKey, te, config);
    }

    static void showChestInfo(ProbeMode mode, IProbeInfo probeInfo, Entity entity, IProbeConfig config) {
        ResourceLocation registryKey = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        showChestInfo(mode, probeInfo, registryKey, entity, config);
    }

    private static void showChestInfo(ProbeMode mode, IProbeInfo probeInfo, ResourceLocation registryKey, ICapabilityProvider entity, IProbeConfig config) {
        List<ItemStack> stacks = null;
        IProbeConfig.ConfigMode chestMode = config.getShowChestContents();
        if (chestMode == IProbeConfig.ConfigMode.EXTENDED && (Config.showSmallChestContentsWithoutSneaking.get() > 0 || !Config.getInventoriesToShow().isEmpty())) {
            if (Config.getInventoriesToShow().contains(registryKey)) {
                chestMode = IProbeConfig.ConfigMode.NORMAL;
            } else if (Config.showSmallChestContentsWithoutSneaking.get() > 0) {
                stacks = new ArrayList<>();
                int slots = getChestContents(entity, registryKey, stacks);
                if (slots <= Config.showSmallChestContentsWithoutSneaking.get()) {
                    chestMode = IProbeConfig.ConfigMode.NORMAL;
                }
            }
        } else if (chestMode == IProbeConfig.ConfigMode.NORMAL && !Config.getInventoriesToNotShow().isEmpty()) {
            if (Config.getInventoriesToNotShow().contains(registryKey)) {
                chestMode = IProbeConfig.ConfigMode.EXTENDED;
            }
        }

        if (Tools.show(mode, chestMode)) {
            if (stacks == null) {
                stacks = new ArrayList<>();
                getChestContents(entity, registryKey, stacks);
            }

            if (!stacks.isEmpty()) {
                boolean showDetailed = Tools.show(mode, config.getShowChestContentsDetailed()) && stacks.size() <= Config.showItemDetailThresshold.get();
                showChestContents(probeInfo, stacks, showDetailed);
            }
        }
    }

    private static void addItemStack(List<ItemStack> stacks, Set<Item> foundItems, @Nonnull ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        if (foundItems != null && foundItems.contains(stack.getItem())) {
            for (ItemStack s : stacks) {
                if (ItemHandlerHelper.canItemStacksStack(s, stack)) {
                    s.grow(stack.getCount());
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

    private static void showChestContents(IProbeInfo probeInfo, List<ItemStack> stacks, boolean detailed) {
        int rows = 0;
        int idx = 0;

        IProbeInfo horizontal = null;
        IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor).spacing(0));

        if (detailed) {
            for (ItemStack stackInSlot : stacks) {
                horizontal = vertical.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER));
                horizontal.item(stackInSlot, new ItemStyle().width(16).height(16))
                        .text(CompoundText.create().info(stackInSlot.getDescriptionId()));
            }
        } else {
            for (ItemStack stackInSlot : stacks) {
                if (idx % 10 == 0) {
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

    private static int getChestContents(ICapabilityProvider inventory, ResourceLocation registryKey, List<ItemStack> stacks) {
        Set<Item> foundItems = Config.compactEqualStacks.get() ? new HashSet<>() : null;
        AtomicInteger maxSlots = new AtomicInteger();
        try {
            if (inventory != null && inventory.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                inventory.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
                    maxSlots.set(capability.getSlots());
                    for (int i = 0; i < maxSlots.get(); i++) {
                        addItemStack(stacks, foundItems, capability.getStackInSlot(i));
                    }

                });
            } else if (inventory instanceof Container container) {
                maxSlots.set(container.getContainerSize());
                for (int i = 0; i < maxSlots.get(); i++) {
                    addItemStack(stacks, foundItems, container.getItem(i));
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Getting the contents of a " + registryKey + " (" + inventory.getClass().getName() + ")", e);
        }
        return maxSlots.get();
    }
}
