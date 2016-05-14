package mcjty.theoneprobe.api;

import net.minecraft.block.Block;

public interface IProbeInfoProvider {

    /**
     * Return true if this block needs information server side. Return false if
     * everything this block wants to display can be calculated on the client.
     * @param block
     * @return
     */
    boolean needsServerInfo(Block block);
}
