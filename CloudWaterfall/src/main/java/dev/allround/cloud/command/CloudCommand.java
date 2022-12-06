package dev.allround.cloud.command;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.player.ICloudPlayer;
import dev.allround.cloud.player.IPlayerManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CloudCommand extends Command {
    public CloudCommand() {
        super("Cloud", "cloud.command.cloud", "CurseCloud");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        String commandLine = String.join(" ",strings);
        if (commandSender instanceof ProxiedPlayer proxiedPlayer){
            ICloudPlayer sender = Cloud.getModule().getComponent(IPlayerManager.class).getCloudPlayer(proxiedPlayer.getUniqueId()).get();
            Cloud.getModule().getComponent(ICommandManager.class).parseLine(commandLine,sender);
        }else {
            ICommandSender sender = new ConsoleSender();
            Cloud.getModule().getComponent(ICommandManager.class).parseLine(commandLine,sender);
        }
    }
}
