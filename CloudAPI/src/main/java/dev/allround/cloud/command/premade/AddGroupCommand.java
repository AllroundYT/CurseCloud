package dev.allround.cloud.command.premade;

import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;

public class AddGroupCommand implements ICommand {
    @Override
    public boolean onExecute(ICommandSender sender, String command, String[] args) {
        if (args.length == 7 && args[0].equalsIgnoreCase("service")) {
        } else {
            sendHelpMessage(sender);
        }

        return true;
    }

    private void sendHelpMessage(ICommandSender commandSender) {
        commandSender.sendMessage("Please use this command like this:");
        commandSender.sendMessage("addgroup <PROXY:SERVICE> <Name (String)> <max. Ram in MegaByte (int)> <max. Players (int)> <MinOnlineAmount (int)> <MaxOnlineAmount (int)> <Service version (only for Services; double)>");
    }
}
