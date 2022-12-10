package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.gui.GuiConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Probe extends Item {

    public Probe() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide) {
            GuiConfig.open();
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

}
