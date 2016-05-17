package mcjty.theoneprobe.api;

/**
 * A mode that indicates what kind of information we want to display
 */
public enum ProbeMode {
    NORMAL,         // Normal display. What a user expects to see
    EXTENDED,       // Extended. This is used whtn eh player is sneaking
    DEBUG           // Creative only. This is used when the player holds a creative probe
}
