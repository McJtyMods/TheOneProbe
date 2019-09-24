package mcjty.theoneprobe.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcjty.theoneprobe.gui.DummyConfigContainer;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CommandTopCfg implements Command<CommandSource> {

    private static final CommandTopCfg CMD = new CommandTopCfg();

    private CommandTopCfg() {}

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("config")
                .requires(cs -> cs.hasPermissionLevel(0))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        NetworkHooks.openGui(player, new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new StringTextComponent("TOP Config");
            }

            @Nullable
            @Override
            public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                return new DummyConfigContainer(id);
            }
        }, player.getPosition());

        return 0;
    }
}
        /*implements ICommand {



    @Override
    public String getName() {
        return "topcfg";
    }

    @Override
    public String getUsage(ICommandSender sender) {
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
        SUBCOMMANDS.put("transparent", s -> Config.setBoxStyle(0, 0, 0, 0));
        SUBCOMMANDS.put("setpos", CommandTopCfg::setPos);
        SUBCOMMANDS.put("opaque", s -> Config.setBoxStyle(2, 0xff999999, 0xff003366, 0));
        SUBCOMMANDS.put("default", s -> Config.setBoxStyle(2, 0xff999999, 0x55006699, 0));
        SUBCOMMANDS.put("liquids", s -> Config.setLiquids(true));
        SUBCOMMANDS.put("noliquids", s -> Config.setLiquids(false));
        SUBCOMMANDS.put("compactequalstacks", s -> Config.setCompactEqualStacks(true));
        SUBCOMMANDS.put("dontcompactequalstacks", s -> Config.setCompactEqualStacks(false));
        SUBCOMMANDS.put("extendedinmain", s -> Config.setExtendedInMain(true));
        SUBCOMMANDS.put("defaultinmain", s -> Config.setExtendedInMain(false));
    }


    private static void setPos(String[] args) {
        if (args.length != 5) {
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
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length < 1) {
            ClientProxy.ignoreNextGuiClose = true;
            PlayerEntitySP player = Minecraft.getInstance().player;
            player.openGui(TheOneProbe.instance, GuiProxy.GUI_CONFIG, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
            return;
        }
        String cmd = args[0];
        Consumer<String[]> consumer = SUBCOMMANDS.get(cmd);
        if (consumer == null) {
            ((PlayerEntity)sender).sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Unknown style option!"), false);
        } else {
            consumer.accept(args);
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return CommandBase.getListOfStringsMatchingLastWord(args, SUBCOMMANDS.keySet());
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