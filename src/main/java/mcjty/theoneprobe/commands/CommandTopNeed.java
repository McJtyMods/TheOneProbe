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
import net.neoforged.neoforge.network.PacketDistributor;

public class CommandTopNeed implements Command<CommandSourceStack> {

    private static final CommandTopNeed CMD = new CommandTopNeed();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("need")
                .requires(cs -> cs.hasPermission(0))
                .executes(CMD);
    }


    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        PacketDistributor.sendToPlayer(player, new PacketOpenGui(PacketOpenGui.GUI_NOTE));
        return 0;
    }
}
