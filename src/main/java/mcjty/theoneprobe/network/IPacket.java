package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Identifier;

public interface IPacket {
    void fromBytes(ByteBuf buf);

    void toBytes(ByteBuf buf);

    Identifier getId();
}
