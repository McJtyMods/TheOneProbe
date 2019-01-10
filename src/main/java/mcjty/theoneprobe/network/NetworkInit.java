package mcjty.theoneprobe.network;

import io.netty.buffer.Unpooled;
import mcjty.theoneprobe.TheOneProbe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.networking.CustomPayloadPacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.CustomPayloadClientPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadServerPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class NetworkInit implements ModInitializer {


    @Override
    public void onInitialize() {
        System.out.println("############ Set up networking #############");

        // Server side
        CustomPayloadPacketRegistry.SERVER.register(PacketGetInfo.GET_INFO, new PacketGetInfo.Handler());
        CustomPayloadPacketRegistry.SERVER.register(PacketGetEntityInfo.GET_ENTITY_INFO, new PacketGetEntityInfo.Handler());

        // Client side
        CustomPayloadPacketRegistry.CLIENT.register(PacketReturnInfo.RETURN_INFO, new PacketReturnInfo.Handler());
        CustomPayloadPacketRegistry.CLIENT.register(PacketReturnEntityInfo.RETURN_ENTITY_INFO, new PacketReturnEntityInfo.Handler());

    }

    @Environment(EnvType.CLIENT)
    public static void sendToServer(IPacket packet) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.toBytes(buf);
        MinecraftClient.getInstance().getNetworkHandler().getClientConnection().sendPacket(new CustomPayloadServerPacket(packet.getId(), buf));

    }

    public static void sendToClient(IPacket packet, ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.toBytes(buf);
        player.networkHandler.sendPacket(new CustomPayloadClientPacket(packet.getId(), buf));

    }
}
