package mcjty.theoneprobe;

import mcjty.theoneprobe.commands.ModCommands;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    // @todo 1.15
//    @SubscribeEvent
//    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
//        Config.setupStyleConfig();
//        Config.updateDefaultOverlayStyle();
//    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.spawnNote.get()) {
            Player player = event.getEntity();
            if (!player.getData(TheOneProbe.ATTACHMENT_TYPE_PLAYER_GOT_NOTE)) {
                if (event.getEntity().getInventory().add(new ItemStack(ModItems.PROBE_NOTE))) {
                    player.setData(TheOneProbe.ATTACHMENT_TYPE_PLAYER_GOT_NOTE, true);
                }
            }
        }
    }
}