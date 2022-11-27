package dev.allround.cloud.command.commands;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.service.ServiceVersion;
import dev.allround.cloud.servicegroup.IServiceGroupManager;
import dev.allround.cloud.servicegroup.ServiceGroup;

public class TestGroupCommand implements ICommand {
    @Override
    public void onExecute(ICommandSender sender, String command, String[] args) {
        sender.sendMessage("Creating test Group...");
        ServiceGroup serviceGroup = new ServiceGroup(ServiceType.SERVER, Cloud.getModule().getModuleInfo().name(),"TestGroup", ServiceVersion.SPIGOT_1_18_2);
        sender.sendMessage("Group creation done. ("+serviceGroup.getGroupName()+":"+serviceGroup.getNode()+")");
        sender.sendMessage("Registering group... ("+serviceGroup.getGroupName()+":"+serviceGroup.getNode()+")");
        Cloud.getModule().getComponent(IServiceGroupManager.class).registerServiceGroup(serviceGroup);
        sender.sendMessage("Group registration done. ("+serviceGroup.getGroupName()+":"+serviceGroup.getNode()+")");
    }
}
