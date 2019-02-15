package mcjty.theoneprobe.items;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class CreativeProbe extends Item {

    public CreativeProbe() {
        super(new Properties()
                .maxStackSize(1)
                .group(TheOneProbe.tabProbe)
        );
        setRegistryName("creativeprobe");
    }

    public void initModel() {
        // @todo 1.13
//        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }


}
