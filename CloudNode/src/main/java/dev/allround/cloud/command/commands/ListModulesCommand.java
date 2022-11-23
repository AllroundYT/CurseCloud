package dev.allround.cloud.command.commands;

import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;

public class ListModulesCommand implements ICommand {
    @Override
    public void onExecute(ICommandSender sender, String command, String[] args) {
        sender.sendMessage("");
        sender.sendMessage("Connected Modules: ");
        ModuleWrapper.getInstance().getModuleInfos().forEach(moduleInfo -> sender.sendMessage(" -> " + moduleInfo.toString()));
        sender.sendMessage("");
    }
}
