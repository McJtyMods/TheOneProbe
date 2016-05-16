package mcjty.theoneprobe.proxy;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.ForgeEventHandlers;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

public abstract class CommonProxy {

    private Configuration mainConfig;

    public void preInit(FMLPreInitializationEvent e) {
        mainConfig = TheOneProbe.config;
        readMainConfig();
        PacketHandler.registerMessages("combathelp");
        ModItems.init();
        ModItems.initCrafting();
    }

    private void readMainConfig() {
        Configuration cfg = mainConfig;
        try {
            cfg.load();
            cfg.addCustomCategoryComment(Config.CATEGORY_THEONEPROBE, "Combat Help configuration");
            Config.init(cfg);
        } catch (Exception e1) {
            TheOneProbe.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (mainConfig.hasChanged()) {
                mainConfig.save();
            }
        }
    }

    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }
        mainConfig = null;
    }

}
