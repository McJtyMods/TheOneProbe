package mcjty.theoneprobe.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcjty.theoneprobe.network.PacketHandler;
import mcjty.theoneprobe.network.PacketOpenGui;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

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
        ServerPlayNetworking.send(player, PacketHandler.PACKET_OPEN_GUI, new PacketOpenGui(PacketOpenGui.GUI_NOTE).toBytes());
        return 0;
    }
}

/*implements ICommand {


    @Override
    public String getName() {
        return "topneed";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "topneed";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        ClientProxy.ignoreNextGuiClose = true;
        PlayerEntitySP player = Minecraft.getInstance().player;
        player.openGui(TheOneProbe.instance, GuiProxy.GUI_NOTE, player.getEntityWorld(), (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ());
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return getName().compareTo(o.getName());
    }
}
*/