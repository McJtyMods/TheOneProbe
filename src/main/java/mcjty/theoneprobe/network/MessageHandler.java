package mcjty.theoneprobe.network;

import net.fabricmc.fabric.networking.PacketContext;
import net.minecraft.util.PacketByteBuf;

import java.util.function.BiConsumer;

public abstract class MessageHandler<T extends IPacket> implements BiConsumer<PacketContext, PacketByteBuf> {

    abstract protected T createPacket();

    @Override
    public void accept(PacketContext context, PacketByteBuf packetByteBuf) {
        T packet = createPacket();
        packet.fromBytes(packetByteBuf);
        context.getTaskQueue().execute(() -> handle(context, packet));

    }

    abstract void handle(PacketContext context, T message);
}
