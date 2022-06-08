package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.world.item.Item;

public class CreativeProbe extends Item {

    public CreativeProbe() {
        super(new Properties()
                .stacksTo(1)
                .tab(TheOneProbe.tabProbe)
        );
    }

}
