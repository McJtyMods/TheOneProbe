package mcjty.theoneprobe.api;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;


/**
 * A mode that indicates what kind of information we want to display.
 * In your IProbeInfoAccessor or IProbeInfoProvider you can use this mode
 * to show different information.
 */
public enum ProbeMode {
    NORMAL,         // Normal display. What a user expects to see
    EXTENDED,       // Extended. This is used when the player is sneaking
    DEBUG;          // Creative only. This is used when the player holds a creative probe

    public static final StreamCodec<FriendlyByteBuf, ProbeMode> STREAM_CODEC = StreamCodec.of((buf, mode) -> buf.writeEnum(mode), buf -> buf.readEnum(ProbeMode.class));
}
