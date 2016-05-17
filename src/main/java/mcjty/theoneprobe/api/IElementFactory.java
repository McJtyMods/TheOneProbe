package mcjty.theoneprobe.api;

import io.netty.buffer.ByteBuf;

/**
 * A factory for elements
 */
public interface IElementFactory {

    /**
     * Create an element from a network buffer.
     */
    IElement createElement(ByteBuf buf);
}
