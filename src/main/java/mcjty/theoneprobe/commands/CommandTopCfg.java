package mcjty.theoneprobe.commands;

import mcjty.theoneprobe.Config;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;

public class CommandTopCfg implements ICommand {

    @Override
    public String getCommandName() {
        return "topcfg";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        String args = StringUtils.join(SUBCOMMANDS.keySet(), " | ");
        return "topcfg [ " + args + " ]";
    }

    private static Map<String,Consumer<String[]>> SUBCOMMANDS = new HashMap<>();

    static {
        SUBCOMMANDS.put("center", s -> Config.setPos(-1, -1, -1, -1));
        SUBCOMMANDS.put("topleft", s -> Config.setPos(5, 5, -1, -1));
        SUBCOMMANDS.put("topcenter", s -> Config.setPos(-1, 5, -1, -1));
        SUBCOMMANDS.put("topright", s -> Config.setPos(-1, 5, 5, -1));
        SUBCOMMANDS.put("bottomleft", s -> Config.setPos(5, -1, -1, 20));
        SUBCOMMANDS.put("bottomcenter", s -> Config.setPos(-1, -1, -1, 20));
        SUBCOMMANDS.put("bottomright", s -> Config.setPos(-1, -1, 5, 20));
        SUBCOMMANDS.put("centerleft", s -> Config.setPos(5, -1, -1, -1));
        SUBCOMMANDS.put("centerright", s -> Config.setPos(-1, -1, 5, -1));
        SUBCOMMANDS.put("transparent", s -> Config.setBoxStyle(0, 0, 0));
        SUBCOMMANDS.put("setpos", CommandTopCfg::setPos);
        SUBCOMMANDS.put("opaque", s -> Config.setBoxStyle(2, 0xff999999, 0xff003366));
        SUBCOMMANDS.put("default", s -> Config.setBoxStyle(2, 0xff999999, 0x55006699));
    }


    private static void setPos(String[] args) {
        if (args.length != 4) {
            return;
        }
        try {
            int leftx = Integer.parseInt(args[1]);
            int topy = Integer.parseInt(args[2]);
            int rightx = Integer.parseInt(args[3]);
            int bottomy = Integer.parseInt(args[4]);
            Config.setPos(leftx, topy, rightx, bottomy);
        } catch (NumberFormatException e) {
        }
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Too few parameters"));
            return;
        }
        String cmd = args[0];
        Consumer<String[]> consumer = SUBCOMMANDS.get(cmd);
        if (consumer == null) {
            sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Unknown style option!"));
        } else {
            consumer.accept(args);
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>(SUBCOMMANDS.keySet());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return getCommandName().compareTo(o.getCommandName());
    }
}
