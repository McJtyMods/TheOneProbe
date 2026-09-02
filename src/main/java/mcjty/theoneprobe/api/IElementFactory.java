package mcjty.theoneprobe.api;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

/**
 * A factory for elements
 */
public interface IElementFactory {

    /**
     * Create an element from a network buffer. This should be
     * symmetrical to what IElement.toBytes() creates.
     */
    IElement createElement(RegistryFriendlyByteBuf buf);

    /**
     * Return a unique resource location that identifies this element factory
     */
    Identifier getId();
}
