package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CreativeProbe extends Item {

    public CreativeProbe() {
        super(new Settings()
                .stackSize(1)
                .itemGroup(ItemGroup.DECORATIONS));

        // @todo fabric, registration
        Registry.ITEM.register(new Identifier(TheOneProbe.MODID, "creativeprove"), this);
    }

}
