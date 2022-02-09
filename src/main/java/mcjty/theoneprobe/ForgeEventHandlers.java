package mcjty.theoneprobe;

import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.playerdata.PlayerGotNote;
import mcjty.theoneprobe.playerdata.PlayerProperties;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ForgeEventHandlers {

    // @todo 1.15
//    @SubscribeEvent
//    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
//        Config.setupStyleConfig();
//        Config.updateDefaultOverlayStyle();
//    }

    public static void onPlayerCloned(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
        if (alive) {
            // We need to copyFrom the capabilities
            PlayerProperties.getPlayerGotNote(newPlayer).copyFrom(PlayerProperties.getPlayerGotNote(oldPlayer));
        }
    }

    public static void onPlayerLoggedIn(ServerPlayer player) {
        if (Config.spawnNote.get()) {
            PlayerGotNote note = PlayerProperties.getPlayerGotNote(player);
            if (!note.isPlayerGotNote()) {
                if (player.getInventory().add(new ItemStack(ModItems.PROBE_NOTE))) {
                    note.setPlayerGotNote(true);
                }
            }
        }
    }
}