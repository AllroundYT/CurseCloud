package dev.allround.cloud.command.premade;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;

public class InfoCommand implements ICommand {
    @Override
    public void onExecute(ICommandSender sender, String command, String[] args) {
        sender.sendMessage("------------------- Module Info -------------------");
        sender.sendMessage("Module Version: " + Cloud.getModule().getModuleInfo().version());
        sender.sendMessage("Module Name: " + Cloud.getModule().getModuleInfo().name());
        sender.sendMessage("Module ID: " + Cloud.getModule().getModuleInfo().id());
        sender.sendMessage("---------------------------------------------------");
    }
}
