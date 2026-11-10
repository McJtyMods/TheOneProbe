package mcjty.theoneprobe.api;

/** Implement this and declare a "theoneprobe" Fabric entrypoint to register addon providers. */
@FunctionalInterface
public interface TheOneProbePlugin {
    void register(ITheOneProbe probe);
}
