package mcjty.theoneprobe.network;



public class PacketHandler {
//    public static SimpleNetworkWrapper INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    // @todo fabric
    public static void registerMessages(String channelName) {
//        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
//
//        // Server side
//        INSTANCE.registerMessage(PacketGetInfo.Handler.class, PacketGetInfo.class, nextID(), Side.SERVER);
//        INSTANCE.registerMessage(PacketGetEntityInfo.Handler.class, PacketGetEntityInfo.class, nextID(), Side.SERVER);
//
//        // Client side
//        INSTANCE.registerMessage(PacketReturnInfo.Handler.class, PacketReturnInfo.class, nextID(), Side.CLIENT);
//        INSTANCE.registerMessage(PacketReturnEntityInfo.Handler.class, PacketReturnEntityInfo.class, nextID(), Side.CLIENT);
    }
}
