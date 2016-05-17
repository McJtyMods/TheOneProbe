package mcjty.theoneprobe;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.DefaultProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = TheOneProbe.MODID, name="TheOneProbe", dependencies =
        "required-after:Forge@["+ TheOneProbe.MIN_FORGE_VER+",)",
        version = TheOneProbe.VERSION)
public class TheOneProbe {
    public static final String MODID = "theoneprobe";
    public static final String VERSION = "1.0.0";
    public static final String MIN_FORGE_VER = "12.16.1.1896";

    @SidedProxy(clientSide="mcjty.theoneprobe.proxy.ClientProxy", serverSide="mcjty.theoneprobe.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance("TheOneProbe")
    public static TheOneProbe instance;
    public static Logger logger;
    public static File mainConfigDir;
    public static File modConfigDir;
    public static Configuration config;

    public static TheOneProbeImp theOneProbeImp = new TheOneProbeImp();

    public static CreativeTabs tabProbe = new CreativeTabs("Probe") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return ModItems.probe;
        }
    };


    /**
     * Run before anything else. Read your config, create blocks, items, etc, and
     * register them with the GameRegistry.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        theOneProbeImp.registerProvider(new DefaultProbeInfoProvider());

        logger = e.getModLog();
        mainConfigDir = e.getModConfigurationDirectory();
        modConfigDir = new File(mainConfigDir.getPath());
        config = new Configuration(new File(modConfigDir, "theoneprobe.cfg"));
        proxy.preInit(e);

//        FMLInterModComms.sendMessage("Waila", "register", "mcjty.wailasupport.WailaCompatibility.load");
    }

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equalsIgnoreCase("getTheOneProbe")) {
                Optional<Function<ITheOneProbe, Void>> value = message.getFunctionValue(ITheOneProbe.class, Void.class);
                value.get().apply(theOneProbeImp);
            }
        }
    }


    /**
     * Do your mod setup. Build whatever data structures you care about. Register recipes.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    /**
     * Handle interaction with other mods, complete your setup based on this.
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
