package mcjty.theoneprobe;

import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.proxy.ClientProxy;
import mcjty.theoneprobe.proxy.CommonProxy;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class TheOneProbe {
    public static final String MODID = "theoneprobe";
    public static final String VERSION = "1.4.25";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";

    public static CommonProxy proxy = new CommonProxy();
    public static ClientProxy clientProxy = new ClientProxy();

    public static TheOneProbe instance = new TheOneProbe();
    public static Logger logger;
    public static File mainConfigDir;
    public static File modConfigDir;
    // @todo fabric
//    public static Configuration config;

    public static TheOneProbeImp theOneProbeImp = new TheOneProbeImp();

    public static boolean baubles = false;
    public static boolean tesla = false;
    public static boolean redstoneflux = false;

    // @todo fabric
//    public static ItemGroup tabProbe = new ItemGroup(1,  "Probe") { // @todo fabric (what is that number)?
//
//        @Override
//        public ItemStack getIconItem() {
//            return new ItemStack(ModItems.probe);
//        }
//    };
//

    /**
     * Run before anything else. Read your config, create blocks, items, etc, and
     * register them with the GameRegistry.
     */
    public void preInit() {
        logger = null;
        // @todo fabric
//        mainConfigDir = e.getModConfigurationDirectory();
//        modConfigDir = new File(mainConfigDir.getPath());
//        config = new Configuration(new File(modConfigDir, "theoneprobe.cfg"));

//        tesla = Loader.isModLoaded("tesla");
//        if (tesla) {
//            logger.log(Level.INFO, "The One Probe Detected TESLA: enabling support");
//        }

//        redstoneflux = Loader.isModLoaded("redstoneflux");
//        if (redstoneflux) {
//            logger.log(Level.INFO, "The One Probe Detected RedstoneFlux: enabling support");
//        }

//        baubles = Loader.isModLoaded("Baubles") || Loader.isModLoaded("baubles");
//        if (baubles) {
//            if (Config.supportBaubles) {
//                logger.log(Level.INFO, "The One Probe Detected Baubles: enabling support");
//            } else {
//                logger.log(Level.INFO, "The One Probe Detected Baubles but support disabled in config");
//                baubles = false;
//            }
//        }

        proxy.preInit();
    }

//    @Mod.EventHandler
//    public void imcCallback(FMLInterModComms.IMCEvent event) {
//        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
//            if (message.key.equalsIgnoreCase("getTheOneProbe")) {
//                Optional<Function<ITheOneProbe, Void>> value = message.getFunctionValue(ITheOneProbe.class, Void.class);
//                if (value.isPresent()) {
//                    value.get().apply(theOneProbeImp);
//                } else {
//                    logger.warn("Some mod didn't return a valid result with getTheOneProbe!");
//                }
//            }
//        }
//    }

    /**
     * Do your mod setup. Build whatever data structures you care about. Register recipes.
     */
    public void init() {
        proxy.init();
    }

    /**
     * Handle interaction with other mods, complete your setup based on this.
     */
    public void postInit() {
        proxy.postInit();
    }
}
