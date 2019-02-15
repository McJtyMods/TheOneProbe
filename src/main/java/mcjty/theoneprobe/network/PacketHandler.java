package mcjty.theoneprobe.network;


import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(TheOneProbe.MODID, channelName), () -> "1.0", s -> true, s -> true);

        // Server side
        INSTANCE.registerMessage(nextID(), PacketGetInfo.class,
                (msg, buf) -> msg.toBytes(buf),
                buf -> new PacketGetInfo(buf),
                (msg, contextSupplier) -> msg.handle(contextSupplier));
        INSTANCE.registerMessage(nextID(), PacketGetEntityInfo.class,
                (msg, buf) -> msg.toBytes(buf),
                buf -> new PacketGetEntityInfo(buf),
                (msg, contextSupplier) -> msg.handle(contextSupplier));

        // Client side
        INSTANCE.registerMessage(nextID(), PacketReturnInfo.class,
                (msg, buf) -> msg.toBytes(buf),
                buf -> new PacketReturnInfo(buf),
                (msg, contextSupplier) -> msg.handle(contextSupplier));
        INSTANCE.registerMessage(nextID(), PacketReturnEntityInfo.class,
                (msg, buf) -> msg.toBytes(buf),
                buf -> new PacketReturnEntityInfo(buf),
                (msg, contextSupplier) -> msg.handle(contextSupplier));
    }
}
