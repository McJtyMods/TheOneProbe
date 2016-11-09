package mcjty.theoneprobe.playerdata;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PlayerProperties {

    @CapabilityInject(PlayerGotNote.class)
    public static Capability<PlayerGotNote> PLAYER_GOT_NOTE;

    public static PlayerGotNote getPlayerGotNote(EntityPlayer player) {
        return player.getCapability(PLAYER_GOT_NOTE, null);
    }


}
