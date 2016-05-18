package mcjty.theoneprobe.commands;

import mcjty.theoneprobe.Config;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;

public class CommandTopCfg implements ICommand {

    @Override
    public String getCommandName() {
        return "topcfg";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "topcfg [ center | topleft | topcenter | topright | bottomleft | bottomcenter | bottomright | centerleft | centerright | transparent | opaque | default ]";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        System.out.println("args = " + args);
        if (args.length != 1) {
            sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Wrong number of parameters"));
            return;
        }
        String cmd = args[0];
        if ("center".equals(cmd)) {
            Config.setPos(-1, -1, -1, -1);
        } else if ("topleft".equals(cmd)) {
            Config.setPos(5, 5, -1, -1);
        } else if ("topcenter".equals(cmd)) {
            Config.setPos(-1, 5, -1, -1);
        } else if ("topright".equals(cmd)) {
            Config.setPos(-1, 5, 5, -1);
        } else if ("bottomleft".equals(cmd)) {
            Config.setPos(5, -1, -1, 15);
        } else if ("bottomcenter".equals(cmd)) {
            Config.setPos(-1, -1, -1, 15);
        } else if ("bottomright".equals(cmd)) {
            Config.setPos(-1, -1, 5, 15);
        } else if ("centerleft".equals(cmd)) {
            Config.setPos(5, -1, -1, -1);
        } else if ("centerright".equals(cmd)) {
            Config.setPos(-1, -1, 5, -1);
        } else if ("transparent".equals(cmd)) {
            Config.boxThickness = 0;
        } else if ("opaque".equals(cmd)) {
            Config.boxThickness = 2;
            Config.boxBorderColor = 0xff999999;
            Config.boxFillColor = 0xff006699;
        } else if ("default".equals(cmd)) {
            Config.boxThickness = 2;
            Config.boxBorderColor = 0xff999999;
            Config.boxFillColor = 0x55006699;
        } else {
            sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Unknown style option!"));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return Collections.emptyList();
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
