package mcjty.theoneprobe.api;

import net.minecraft.item.ItemStack;

/**
 * Information to return to the probe. Most methods here return the same probe info
 * object so that you can chain:
 *     probeInfo.item(diamond).text("My diamond").text("Next line");
 *
 * horizontal() and vertical() are exceptions. They return a new probe info object
 * representing the new horizontal or vertical layout that was created. Keep that in mind!
 *
 * Note that you can safeul use TextFormatting color codes in the text.
 */
public interface IProbeInfo {

    IProbeInfo text(String text);

    IProbeInfo item(ItemStack stack);

    /**
     * This creates a progress bar of 100 width
     */
    IProbeInfo progress(int current, int max, ProgressStyle style);

    /**
     * Create a new horizontal probe info as a child of this one. Note that the returned
     * probe info is the new horizontal layout and not this one!
     */
    IProbeInfo horizontal(LayoutStyle style);
    IProbeInfo horizontal();

    /**
     * Create a new vertical probe info as a child of this one. Note that the returned
     * probe info is the new horizontal layout and not this one!
     */
    IProbeInfo vertical(LayoutStyle style);
    IProbeInfo vertical();

    /**
     * Add a custom element. Make sure the factory for this element is properly registered.
     */
    IProbeInfo element(IElement element);
}
