package mcjty.theoneprobe.api;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

/**
 * A factory for elements
 */
public interface IElementFactoryNew extends IElementFactory {

    /**
     * Create an element from a network buffer. This should be
     * symmetrical to what IElement.toBytes() creates.
     */
    IElement createElement(PacketBuffer buf);

    @Override
    default IElement createElement(ByteBuf buf) {
        if (buf instanceof PacketBuffer) {
            return createElement((PacketBuffer) buf);
        }
        return null;
    }
}
