package mcjty.theoneprobe;

import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Optional;
import java.util.function.Function;

@Mod(modid = TheOneProbe.MODID, name="TheOneProbe",
        dependencies =
                "after:forge@[" + TheOneProbe.MIN_FORGE11_VER + ",);" +
                "after:tesla",
        version = TheOneProbe.VERSION,
        guiFactory = "mcjty.theoneprobe.config.TopModGuiFactory")
public class TheOneProbe {
    public static final String MODID = "theoneprobe";
    public static final String VERSION = "1.4.9";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";

    @SidedProxy(clientSide="mcjty.theoneprobe.proxy.ClientProxy", serverSide="mcjty.theoneprobe.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static TheOneProbe instance;
    public static Logger logger;
    public static File mainConfigDir;
    public static File modConfigDir;
    public static Configuration config;

    public static TheOneProbeImp theOneProbeImp = new TheOneProbeImp();

    public static boolean baubles = false;

    public static CreativeTabs tabProbe = new CreativeTabs("Probe") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.probe);
        }
    };


    /**
     * Run before anything else. Read your config, create blocks, items, etc, and
     * register them with the GameRegistry.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        mainConfigDir = e.getModConfigurationDirectory();
        modConfigDir = new File(mainConfigDir.getPath());
        config = new Configuration(new File(modConfigDir, "theoneprobe.cfg"));

        baubles = Loader.isModLoaded("Baubles") || Loader.isModLoaded("baubles");
        if (baubles) {
            if (Config.supportBaubles) {
                logger.log(Level.INFO, "The One Probe Detected Baubles: enabling support");
            } else {
                logger.log(Level.INFO, "The One Probe Detected Baubles but support disabled in config");
                baubles = false;
            }
        }

        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equalsIgnoreCase("getTheOneProbe")) {
                Optional<Function<ITheOneProbe, Void>> value = message.getFunctionValue(ITheOneProbe.class, Void.class);
                if (value.isPresent()) {
                    value.get().apply(theOneProbeImp);
                } else {
                    logger.warn("Some mod didn't return a valid result with getTheOneProbe!");
                }
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
