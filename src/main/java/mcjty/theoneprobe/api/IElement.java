package mcjty.theoneprobe.api;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.apiimpl.elements.ElementType;

/**
 * An element in the probe gui.
 */
public interface IElement {

    /**
     * Render this element at the location given by the cursor
     */
    void render(Cursor cursor);

    /**
     * Get the width of this element
     */
    int getWidth();

    /**
     * Get the height of this element
     */
    int getHeight();

    /**
     * Persist this element to the given network buffer
     */
    void toBytes(ByteBuf buf);

    /**
     * Get the identifier for this element (as returned by ITheOneProbe.registerElementFactory()
     */
    int getID();
}
