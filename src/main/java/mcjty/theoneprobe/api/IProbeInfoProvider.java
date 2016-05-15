package mcjty.theoneprobe.api;

import net.minecraft.block.Block;

/**
 * You can implement IProbeInfoAccessor in your blocks or else you can use
 * this and register that to the ITheOneProbe.
 */
public interface IProbeInfoProvider {

    /**
     * Return true if this block needs information server side. Return false if
     * everything this block wants to display can be calculated on the client.
     */
    boolean needsServerInfo(Block block);

    /**
     * Create a probe info for the given block. Return null if you don't
     * support this block.
     */
    IProbeInfo createProbeInfo(ITheOneProbe theOneProbe, Block block);
}
