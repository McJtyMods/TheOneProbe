package mcjty.theoneprobe.api;

/**
 * Represent a style for text. This style is configurable by the user and used server-side
 */
public enum TextStyleClass {
    MODNAME,        // Name of the mod
    BLOCKNAME,      // Name of the block
    INFO,           // General info, neutral
    WARNING,        // Warning, something is not ready (not mature), or missing stuff
    ERROR,          // Error, bad error, out of power, things like that
    OK,             // Status ok
    PROGRESS        // Progress rendering in case the bar is not used
}
