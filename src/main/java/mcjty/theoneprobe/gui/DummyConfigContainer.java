package mcjty.theoneprobe.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

import static mcjty.theoneprobe.proxy.Registration.CONTAINER_CONFIG;

public class DummyConfigContainer extends Container {

    public DummyConfigContainer(int id) {
        super(CONTAINER_CONFIG, id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
