package mcjty.theoneprobe.setup;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.concurrent.Callable;

public interface IProxy {

    void preInit(FMLPreInitializationEvent e);

    void init(FMLInitializationEvent e);

    void postInit(FMLPostInitializationEvent e);

    World getClientWorld();

    EntityPlayer getClientPlayer();

    <V> ListenableFuture<V> addScheduledTaskClient(Callable<V> callableToSchedule);

    ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule);
}
