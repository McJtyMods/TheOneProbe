package mcjty.theoneprobe;

import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.playerdata.PlayerGotNote;
import mcjty.theoneprobe.playerdata.PlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

public class ForgeEventHandlers {


    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        // @todo 1.13
//        Config.setupStyleConfig(TheOneProbe.config);
        Config.updateDefaultOverlayStyle();

//        if (TheOneProbe.config.hasChanged()) {
//            TheOneProbe.config.save();
//        }
    }

    @SubscribeEvent
    public void onEntityConstructing(AttachCapabilitiesEvent<Entity> event){
        // @todo 1.13
//        if (event.getObject() instanceof PlayerEntity) {
//            if (!event.getObject().getCapability(PlayerProperties.PLAYER_GOT_NOTE).isPresent()) {
//                event.addCapability(new ResourceLocation(TheOneProbe.MODID, "Properties"), new PropertiesDispatcher());
//            }
//        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            LazyOptional<PlayerGotNote> capability = event.getOriginal().getCapability(PlayerProperties.PLAYER_GOT_NOTE);
            capability.ifPresent(oldStore -> {
                event.getEntityPlayer().getCapability(PlayerProperties.PLAYER_GOT_NOTE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    // @todo 1.13
//    @SubscribeEvent
//    public void onPlayerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
//        if (Config.spawnNote) {
//            PlayerGotNote note = PlayerProperties.getPlayerGotNote(event.getPlayer());
//            if (!note.isPlayerGotNote()) {
//                boolean success = event.getPlayer().inventory.addItemStackToInventory(new ItemStack(ModItems.probeNote));
//                if (success) {
//                    note.setPlayerGotNote(true);
//                }
//            }
//        }
//    }
}