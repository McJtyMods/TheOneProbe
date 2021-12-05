package mcjty.theoneprobe.playerdata;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class PlayerProperties {

    public static Capability<PlayerGotNote> PLAYER_GOT_NOTE
            = CapabilityManager.get(new CapabilityToken<>(){});
}
