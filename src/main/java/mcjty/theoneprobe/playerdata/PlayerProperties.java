package mcjty.theoneprobe.playerdata;

import mcjty.theoneprobe.lib.PlayerGotNoteExtension;
import net.minecraft.world.entity.player.Player;

public class PlayerProperties {

    public static PlayerGotNote getPlayerGotNote(Player player) {
        return ((PlayerGotNoteExtension)player).getPlayerGotNote();
    }
}
