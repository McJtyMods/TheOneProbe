package mcjty.theoneprobe.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcjty.theoneprobe.network.PacketHandler;
import mcjty.theoneprobe.network.PacketOpenGui;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;

public class CommandTopCfg implements Command<CommandSource> {

    private static final CommandTopCfg CMD = new CommandTopCfg();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("config")
                .requires(cs -> cs.hasPermission(0))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrException();
        PacketHandler.INSTANCE.sendTo(new PacketOpenGui(PacketOpenGui.GUI_CONFIG), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        return 0;
    }
}
