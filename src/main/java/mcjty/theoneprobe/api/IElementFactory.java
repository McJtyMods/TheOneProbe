package mcjty.theoneprobe.api;

import io.netty.buffer.ByteBuf;

/**
 * A factory for elements
 *
 * @deprecated To be removed in 1.16, and replaced with {@link IElementFactoryNew}
 */
@Deprecated
public interface IElementFactory {//TODO: 1.16, remove this and replace with IElementFactoryNew

    /**
     * Create an element from a network buffer. This should be
     * symmetrical to what IElement.toBytes() creates.
     */
    @Deprecated
    IElement createElement(ByteBuf buf);
}
