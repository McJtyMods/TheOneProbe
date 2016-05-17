package mcjty.theoneprobe.api;

import net.minecraft.item.ItemStack;

/**
 * Information to return to the probe
 */
public interface IProbeInfo {

    IProbeInfo text(String text);

    IProbeInfo item(ItemStack stack);

    IProbeInfo progress(int current, int max, String prefix, String suffix, ProgressStyle style);

    IProbeInfo newline();

    IProbeInfo offset(int dx, int dy);
}
