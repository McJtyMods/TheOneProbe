package mcjty.theoneprobe.network;

import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.util.PacketByteBuf;

public abstract class MessageHandler<T extends IPacket> implements PacketConsumer {

    abstract protected T createPacket();

    @Override
    public void accept(PacketContext context, PacketByteBuf packetByteBuf) {
        T packet = createPacket();
        packet.fromBytes(packetByteBuf);
        context.getTaskQueue().execute(() -> handle(context, packet));

    }

    abstract void handle(PacketContext context, T message);
}
