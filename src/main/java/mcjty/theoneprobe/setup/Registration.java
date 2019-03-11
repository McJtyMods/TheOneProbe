package mcjty.theoneprobe.setup;


import mcjty.theoneprobe.items.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Registration {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.init();

        event.getRegistry().register(ModItems.probe);
        event.getRegistry().register(ModItems.creativeProbe);
        event.getRegistry().register(ModItems.probeNote);

        event.getRegistry().register(ModItems.diamondHelmetProbe);
        event.getRegistry().register(ModItems.goldHelmetProbe);
        event.getRegistry().register(ModItems.ironHelmetProbe);

        if (ModSetup.baubles) {
            event.getRegistry().register(ModItems.probeGoggles);
        }
    }

}
