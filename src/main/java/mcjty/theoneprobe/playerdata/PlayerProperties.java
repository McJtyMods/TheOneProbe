package mcjty.theoneprobe.playerdata;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PlayerProperties {

    @CapabilityInject(PlayerGotNote.class)
    public static Capability<PlayerGotNote> PLAYER_GOT_NOTE;
}
