package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.gui.DummyConfigContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = TheOneProbe.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {

    @ObjectHolder("theoneprobe:config")
    public static ContainerType<DummyConfigContainer> CONTAINER_CONFIG;

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        ContainerType<Container> containerType = IForgeContainerType.create((windowId, inv, data) -> {
            return new DummyConfigContainer(windowId);
        });
        containerType.setRegistryName("config");
        event.getRegistry().register(containerType);
    }

}
