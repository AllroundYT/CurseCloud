package dev.allround.cloud.command.commands;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.servicegroup.IServiceGroupManager;

public class ListGroupsCommand implements ICommand {
    @Override
    public void onExecute(ICommandSender sender, String command, String[] args) {
        sender.sendMessage("");
        sender.sendMessage("-------------- Group List: --------------");
        Cloud.getModule().getComponent(IServiceGroupManager.class).getServiceGroups().forEach(iServiceGroup ->
                sender.sendMessage(" - "+iServiceGroup.getGroupName()+", "+iServiceGroup.getNode()+", "+iServiceGroup.getOnlineServiceAmount()));
        sender.sendMessage("-----------------------------------------");
        sender.sendMessage("");
    }
}
