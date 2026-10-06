package mcjty.theoneprobe.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class FabricNetworking {
    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(PacketGetInfo.TYPE, PacketGetInfo.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(PacketGetEntityInfo.TYPE, PacketGetEntityInfo.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(PacketReturnInfo.TYPE, PacketReturnInfo.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(PacketReturnEntityInfo.TYPE, PacketReturnEntityInfo.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(PacketOpenGui.TYPE, PacketOpenGui.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(PacketGetInfo.TYPE, (packet, context) -> packet.handle(context));
        ServerPlayNetworking.registerGlobalReceiver(PacketGetEntityInfo.TYPE, (packet, context) -> packet.handle(context));
    }
}
