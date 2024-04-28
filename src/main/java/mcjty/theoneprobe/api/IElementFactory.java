package mcjty.theoneprobe.api;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

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
    ResourceLocation getId();
}
