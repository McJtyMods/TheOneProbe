package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.item.Item;

public class CreativeProbe extends Item {

    public CreativeProbe() {
        super(new Properties()
                .maxStackSize(1)
                .group(TheOneProbe.tabProbe)
        );
        setRegistryName("creativeprobe");
    }

}
