package mcjty.theoneprobe.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class FabricNetworking {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(PacketGetInfo.TYPE, PacketGetInfo.CODEC);
        PayloadTypeRegistry.playC2S().register(PacketGetEntityInfo.TYPE, PacketGetEntityInfo.CODEC);
        PayloadTypeRegistry.playS2C().register(PacketReturnInfo.TYPE, PacketReturnInfo.CODEC);
        PayloadTypeRegistry.playS2C()
                .register(PacketReturnEntityInfo.TYPE, PacketReturnEntityInfo.CODEC);
        PayloadTypeRegistry.playS2C().register(PacketOpenGui.TYPE, PacketOpenGui.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                PacketGetInfo.TYPE, (packet, context) -> packet.handle(context));
        ServerPlayNetworking.registerGlobalReceiver(
                PacketGetEntityInfo.TYPE, (packet, context) -> packet.handle(context));
    }
}
