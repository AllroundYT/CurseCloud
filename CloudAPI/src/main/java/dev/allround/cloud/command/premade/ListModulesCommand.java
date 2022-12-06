package dev.allround.cloud.command.premade;

import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;

public class ListModulesCommand implements ICommand {
    @Override
    public boolean onExecute(ICommandSender sender, String command, String[] args) {
        sender.sendMessage("");
        sender.sendMessage("Connected Modules: ");
        ModuleWrapper.getInstance().getModuleInfos().forEach(moduleInfo -> sender.sendMessage(" -> " + moduleInfo.toString()));
        sender.sendMessage("");
        return true;
    }
}
