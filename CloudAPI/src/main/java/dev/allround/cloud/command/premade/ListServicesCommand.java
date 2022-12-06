package dev.allround.cloud.command.premade;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.service.IServiceManager;

public class ListServicesCommand implements ICommand {
    @Override
    public boolean onExecute(ICommandSender sender, String command, String[] args) {
        sender.sendMessage("");
        sender.sendMessage("-------------- Service List: --------------");
        Cloud.getModule().getComponent(IServiceManager.class).getServices().forEach(iService ->
                sender.sendMessage(" - " + iService.getServiceID() + ", " + iService.getServiceGroup() + ", " + iService.getStatus() + ", " + iService.getNode()));
        sender.sendMessage("-------------------------------------------");
        sender.sendMessage("");
        return true;
    }
}
