package mcjty.theoneprobe.network;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

public class ClientNetworkInit implements ClientModInitializer {

	public void onInitializeClient() {
		System.out.println("############ Set up client-side networking #############");

		ClientSidePacketRegistry.INSTANCE.register(PacketReturnInfo.RETURN_INFO, new PacketReturnInfo.Handler());
		ClientSidePacketRegistry.INSTANCE.register(PacketReturnEntityInfo.RETURN_ENTITY_INFO, new PacketReturnEntityInfo.Handler());
	}
}
