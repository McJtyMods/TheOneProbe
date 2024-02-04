package mcjty.theoneprobe.playerdata;

import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;

public class PlayerProperties {

    public static Capability<PlayerGotNote> PLAYER_GOT_NOTE
            = CapabilityManager.get(new CapabilityToken<>(){});
}
