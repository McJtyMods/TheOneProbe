package mcjty.theoneprobe;

import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.setup.IProxy;
import mcjty.theoneprobe.setup.ModSetup;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Optional;
import java.util.function.Function;

@Mod(modid = TheOneProbe.MODID, name="TheOneProbe",
        dependencies =
                "after:forge@[" + TheOneProbe.MIN_FORGE11_VER + ",);" +
                "after:tesla",
        version = TheOneProbe.VERSION,
        acceptedMinecraftVersions = "[1.12,1.13)",
        guiFactory = "mcjty.theoneprobe.config.TopModGuiFactory")
public class TheOneProbe {
    public static final String MODID = "theoneprobe";
    public static final String VERSION = "1.4.28";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";

    @SidedProxy(clientSide="mcjty.theoneprobe.setup.ClientProxy", serverSide="mcjty.theoneprobe.setup.ServerProxy")
    public static IProxy proxy;
    public static ModSetup setup = new ModSetup();

    @Mod.Instance
    public static TheOneProbe instance;

    public static TheOneProbeImp theOneProbeImp = new TheOneProbeImp();

    public static CreativeTabs tabProbe = new CreativeTabs("Probe") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.probe);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        setup.preInit(e);
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        setup.init(e);
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        setup.postInit(e);
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equalsIgnoreCase("getTheOneProbe")) {
                Optional<Function<ITheOneProbe, Void>> value = message.getFunctionValue(ITheOneProbe.class, Void.class);
                if (value.isPresent()) {
                    value.get().apply(theOneProbeImp);
                } else {
                    setup.getLogger().warn("Some mod didn't return a valid result with getTheOneProbe!");
                }
            }
        }
    }
}
