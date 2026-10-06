package mcjty.theoneprobe.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcjty.theoneprobe.network.PacketOpenGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class CommandTopNeed implements Command<CommandSourceStack> {

    private static final CommandTopNeed CMD = new CommandTopNeed();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("need")
                .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                .executes(CMD);
    }


    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ServerPlayNetworking.send(player, new PacketOpenGui(PacketOpenGui.GUI_NOTE));
        return 0;
    }
}
