package mcjty.theoneprobe.api;

/**
 * Main interface for this mod. Use this to build probe information
 * Get a reference to an implementation of this interface by calling:
 *         FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "<whatever>.YourClass$GetTheOneProbe");
 */
public interface ITheOneProbe {

    /**
     * Optionally register a provider for your probe information. You don't have to do this. You
     * can also implement IProbeInfoAccessor in your block instead.
     * @param provider
     */
    void registerProvider(IProbeInfoProvider provider);

    /**
     * Register an element factory.
     * @return an id to use when defining elements using this factory
     */
    int registerElementFactory(IElementFactory factory);

    /**
     * Get the element factory for a given ID.
     */
    IElementFactory getElementFactory(int id);
}
