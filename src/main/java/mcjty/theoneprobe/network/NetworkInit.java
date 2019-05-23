package mcjty.theoneprobe.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;

public class NetworkInit implements ModInitializer {


    @Override
    public void onInitialize() {
        System.out.println("############ Set up server-side networking #############");

        ServerSidePacketRegistry.INSTANCE.register(PacketGetInfo.GET_INFO, new PacketGetInfo.Handler());
        ServerSidePacketRegistry.INSTANCE.register(PacketGetEntityInfo.GET_ENTITY_INFO, new PacketGetEntityInfo.Handler());
    }

    @Environment(EnvType.CLIENT)
    public static void sendToServer(IPacket packet) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.toBytes(buf);
        MinecraftClient.getInstance().getNetworkHandler().getClientConnection().send(new CustomPayloadC2SPacket(packet.getId(), buf));

    }

    public static void sendToClient(IPacket packet, ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.toBytes(buf);
        player.networkHandler.sendPacket(new CustomPayloadS2CPacket(packet.getId(), buf));

    }
}
