package mcjty.theoneprobe.network;


import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
                PacketGetInfo::toBytes,
                PacketGetInfo::new,
                PacketGetInfo::handle);
        INSTANCE.registerMessage(nextID(), PacketGetEntityInfo.class,
                PacketGetEntityInfo::toBytes,
                PacketGetEntityInfo::new,
                PacketGetEntityInfo::handle);

        // Client side
        INSTANCE.registerMessage(nextID(), PacketReturnInfo.class,
                PacketReturnInfo::toBytes,
                PacketReturnInfo::new,
                PacketReturnInfo::handle);
        INSTANCE.registerMessage(nextID(), PacketReturnEntityInfo.class,
                PacketReturnEntityInfo::toBytes,
                PacketReturnEntityInfo::new,
                PacketReturnEntityInfo::handle);
        INSTANCE.registerMessage(nextID(), PacketOpenGui.class,
                PacketOpenGui::toBytes,
                PacketOpenGui::new,
                PacketOpenGui::handle);
    }
}
