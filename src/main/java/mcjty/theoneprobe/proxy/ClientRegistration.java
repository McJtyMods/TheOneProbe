package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.gui.DummyConfigContainer;
import mcjty.theoneprobe.gui.GuiConfig;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TheOneProbe.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent e) {
        ScreenManager.IScreenFactory<DummyConfigContainer, GuiConfig> factory = (container, inventory, title) -> {
            return new GuiConfig(container, inventory);
        };
        ScreenManager.registerFactory(Registration.CONTAINER_CONFIG, factory);
    }
}
