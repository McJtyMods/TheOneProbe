package mcjty.theoneprobe;

import mcjty.theoneprobe.commands.ModCommands;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.playerdata.PlayerGotNote;
import mcjty.theoneprobe.playerdata.PlayerProperties;
import mcjty.theoneprobe.playerdata.PropertiesDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }


    // @todo 1.15
//    @SubscribeEvent
//    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
//        Config.setupStyleConfig();
//        Config.updateDefaultOverlayStyle();
//    }

    @SubscribeEvent
    public void onEntityConstructing(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerProperties.PLAYER_GOT_NOTE).isPresent()) {
                event.addCapability(new ResourceLocation(TheOneProbe.MODID, "properties"), new PropertiesDispatcher());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            LazyOptional<PlayerGotNote> capability = event.getOriginal().getCapability(PlayerProperties.PLAYER_GOT_NOTE);
            capability.ifPresent(oldStore -> {
                event.getPlayer().getCapability(PlayerProperties.PLAYER_GOT_NOTE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.spawnNote.get()) {
            event.getPlayer().getCapability(PlayerProperties.PLAYER_GOT_NOTE).ifPresent(note -> {
                if (!note.isPlayerGotNote()) {
                    boolean success = event.getPlayer().inventory.add(new ItemStack(ModItems.probeNote));
                    if (success) {
                        note.setPlayerGotNote(true);
                    }
                }
            });
        }
    }
}