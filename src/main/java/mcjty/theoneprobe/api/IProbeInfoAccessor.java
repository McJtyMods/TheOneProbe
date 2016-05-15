package mcjty.theoneprobe.api;

import net.minecraft.block.Block;

/**
 * You can implement this in your block implementation if you want to support
 * the probe. An alternative is to make an IProveInfoProvider.
 */
public interface IProbeInfoAccessor {

    /**
     * Return true if this block needs information server side. Return false if
     * everything this block wants to display can be calculated on the client.
     */
    boolean needsServerInfo();

    /**
     * Create a probe info for this block. Return null if you don't
     * support probe info here.
     */
    IProbeInfo createProbeInfo(ITheOneProbe theOneProbe);
}
