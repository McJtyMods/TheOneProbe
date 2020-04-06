package mcjty.theoneprobe.api;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

public interface IElementNew extends IElement {

    /**
     * Persist this element to the given network buffer. This should be symmetrical to
     * what IElementFactory.createElement() expects.
     */
    void toBytes(PacketBuffer buf);

    @Override
    @Deprecated
    default void toBytes(ByteBuf buf) {
        if (buf instanceof PacketBuffer) {
            toBytes((PacketBuffer) buf);
        } else {
            toBytes(new PacketBuffer(buf));
        }
    }
}