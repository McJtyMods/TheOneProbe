package mcjty.theoneprobe.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> cmdTop = dispatcher.register(
                Commands.literal(TheOneProbe.MODID)
                        .then(CommandTopCfg.register(dispatcher))
                        .then(CommandTopNeed.register(dispatcher))
        );

        dispatcher.register(Commands.literal("top").redirect(cmdTop));
    }

}
