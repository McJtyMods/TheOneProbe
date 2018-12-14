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

    public static final Identifier GET_INFO = new Identifier(TheOneProbe.MODID, "get_info");
    public static final Identifier RETURN_INFO = new Identifier(TheOneProbe.MODID, "return_info");

    public static final Identifier GET_ENTITY_INFO = new Identifier(TheOneProbe.MODID, "get_entity_info");
    public static final Identifier RETURN_ENTITY_INFO = new Identifier(TheOneProbe.MODID, "return_entity_info");

    @Override
    public void onInitialize() {
        System.out.println("############ Set up networking #############");

        // Server side
        CustomPayloadPacketRegistry.SERVER.register(GET_INFO, new PacketGetInfo.Handler());
        CustomPayloadPacketRegistry.SERVER.register(GET_ENTITY_INFO, new PacketGetEntityInfo.Handler());

        // Client side
        CustomPayloadPacketRegistry.CLIENT.register(RETURN_INFO, new PacketReturnInfo.Handler());
        CustomPayloadPacketRegistry.CLIENT.register(RETURN_ENTITY_INFO, new PacketReturnEntityInfo.Handler());

    }

    @Environment(EnvType.SERVER)
    public static void returnInfo(PacketReturnInfo packet, ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.toBytes(buf);
        player.networkHandler.sendPacket(new CustomPayloadClientPacket(RETURN_INFO, buf));
    }

    @Environment(EnvType.CLIENT)
    public static void getInfo(PacketGetInfo packet) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.toBytes(buf);
        MinecraftClient.getInstance().getNetworkHandler().getClientConnection().sendPacket(new CustomPayloadServerPacket(GET_INFO, buf));
    }

    @Environment(EnvType.SERVER)
    public static void returnEntityInfo(PacketReturnEntityInfo packet, ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.toBytes(buf);
        player.networkHandler.sendPacket(new CustomPayloadClientPacket(RETURN_ENTITY_INFO, buf));
    }

    @Environment(EnvType.CLIENT)
    public static void getEntityInfo(PacketGetEntityInfo packet) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        packet.toBytes(buf);
        MinecraftClient.getInstance().getNetworkHandler().getClientConnection().sendPacket(new CustomPayloadServerPacket(GET_ENTITY_INFO, buf));
    }

}
