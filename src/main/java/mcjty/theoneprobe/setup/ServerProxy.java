package mcjty.theoneprobe.setup;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.concurrent.Callable;

public class ServerProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
    }

    @Override
    public void init(FMLInitializationEvent e) {
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
    }


    @Override
    public World getClientWorld() {
        throw new IllegalStateException("This should only be called from client side");
    }

    @Override
    public EntityPlayer getClientPlayer() {
        throw new IllegalStateException("This should only be called from client side");
    }

    @Override
    public <V> ListenableFuture<V> addScheduledTaskClient(Callable<V> callableToSchedule) {
        throw new IllegalStateException("This should only be called from client side");
    }

    @Override
    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
        throw new IllegalStateException("This should only be called from client side");
    }
}