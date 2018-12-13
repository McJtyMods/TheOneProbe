package mcjty.theoneprobe.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class CreativeProbe extends Item {

    public CreativeProbe() {
        super(new Settings()
                .stackSize(1)
                .itemGroup(ItemGroup.DECORATIONS));
    }

}
