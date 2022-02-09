package mcjty.theoneprobe.network;


import mcjty.theoneprobe.TheOneProbe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler {
    private static int ID = 0;
    // Server ID's
    public static final ResourceLocation PACKET_GET_INFO = TheOneProbe.asResource("packet_get_info");
    public static final ResourceLocation PACKET_GET_ENTITY_INFO = TheOneProbe.asResource("packet_get_entity_info");
    // Client ID's
    public static final ResourceLocation PACKET_RETURN_INFO = TheOneProbe.asResource("packet_return_info");
    public static final ResourceLocation PACKET_RETURN_ENTITY_INFO = TheOneProbe.asResource("packet_return_enity_info");
    public static final ResourceLocation PACKET_OPEN_GUI = TheOneProbe.asResource("packet_open_gui");

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        // Server side
        ServerPlayNetworking.registerGlobalReceiver(PACKET_GET_INFO, (server, player, handler, buf, responseSender) -> {
            PacketGetInfo packetGetInfo = new PacketGetInfo(buf);
            packetGetInfo.handle(server, player);
        });
        ServerPlayNetworking.registerGlobalReceiver(PACKET_GET_ENTITY_INFO, (server, player, handler, buf, responseSender) -> {
            PacketGetEntityInfo packetGetEntityInfo = new PacketGetEntityInfo(buf);
            packetGetEntityInfo.handle(server, player);
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerMessagesClient() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_RETURN_INFO, (client, handler, buf, responseSender) -> {
            PacketReturnInfo packetReturnInfo = new PacketReturnInfo(buf);
            packetReturnInfo.handle(client);
        });
        ClientPlayNetworking.registerGlobalReceiver(PACKET_RETURN_ENTITY_INFO, (client, handler, buf, responseSender) -> {
            PacketReturnEntityInfo packetReturnEntityInfo = new PacketReturnEntityInfo(buf);
            packetReturnEntityInfo.handle(client);
        });
        ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_GUI, (client, handler, buf, responseSender) -> {
            PacketOpenGui packetOpenGui = new PacketOpenGui(buf);
            packetOpenGui.handle(client);
        });
    }
}
