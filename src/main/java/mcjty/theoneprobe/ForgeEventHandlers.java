package mcjty.theoneprobe;

import mcjty.theoneprobe.commands.ModCommands;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.network.*;
import mcjty.theoneprobe.playerdata.PlayerGotNote;
import mcjty.theoneprobe.playerdata.PlayerProperties;
import mcjty.theoneprobe.playerdata.PropertiesDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

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
            event.getOriginal().getCapability(PlayerProperties.PLAYER_GOT_NOTE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerProperties.PLAYER_GOT_NOTE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.spawnNote.get()) {
            event.getEntity().getCapability(PlayerProperties.PLAYER_GOT_NOTE).ifPresent(note -> {
                if (!note.isPlayerGotNote()) {
                    if (event.getEntity().getInventory().add(new ItemStack(ModItems.PROBE_NOTE))) {
                        note.setPlayerGotNote(true);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerGotNote.class);
    }

    @SubscribeEvent
    public void onRegisterPayloadHandler(RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(TheOneProbe.MODID)
                .versioned("1.0")
                .optional();
        registrar.play(PacketGetEntityInfo.ID, PacketGetEntityInfo::create, handler -> handler
                .server(PacketGetEntityInfo::handle));
        registrar.play(PacketReturnEntityInfo.ID, PacketReturnEntityInfo::create, handler -> handler
                .client(PacketReturnEntityInfo::handle));
        registrar.play(PacketGetInfo.ID, PacketGetInfo::create, handler -> handler
                .server(PacketGetInfo::handle));
        registrar.play(PacketOpenGui.ID, PacketOpenGui::create, handler -> handler
                .client(PacketOpenGui::handle));
        registrar.play(PacketReturnInfo.ID, PacketReturnInfo::create, handler -> handler
                .client(PacketReturnInfo::handle));
    }
}