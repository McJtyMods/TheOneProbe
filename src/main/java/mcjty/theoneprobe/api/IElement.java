package mcjty.theoneprobe.api;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

/**
 * An element in the probe gui.
 */
public interface IElement {

    /**
     * Render this element at the location given by the location
     */
    void render(int x, int y);

    /**
     * Get the width of this element
     */
    int getWidth();

    /**
     * Get the height of this element
     */
    int getHeight();

    /**
     * Persist this element to the given network buffer. This should be symmetrical to
     * what IElementFactory.createElement() expects.
     *
     * @deprecated To be removed in 1.16, prefer implementing and using {@link #toBytes(PacketBuffer)}
     *
     * @implNote NO-OPing this method is fine as long as {@link #toBytes(PacketBuffer)} is properly implemented
     */
    @Deprecated
    default void toBytes(ByteBuf buf) {
        //TODO: 1.16 remove this method, it is being left for binary compatibility for now
    }

    /**
     * Persist this element to the given network buffer. This should be symmetrical to
     * what IElementFactory.createElement() expects.
     */
    default void toBytes(PacketBuffer buf) {
        toBytes((ByteBuf) buf);
    }

    /**
     * Get the identifier for this element (as returned by ITheOneProbe.registerElementFactory()
     */
    int getID();
}
