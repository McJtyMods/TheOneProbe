package mcjty.theoneprobe.api;

import net.minecraft.network.PacketBuffer;

/**
 * A factory for elements
 */
public interface IElementFactory {

    /**
     * Create an element from a network buffer. This should be
     * symmetrical to what IElement.toBytes() creates.
     */
    IElement createElement(PacketBuffer buf);
}
