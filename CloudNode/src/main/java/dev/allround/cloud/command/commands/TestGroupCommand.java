package dev.allround.cloud.command.commands;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.service.ServiceVersion;
import dev.allround.cloud.servicegroup.IServiceGroup;
import dev.allround.cloud.servicegroup.IServiceGroupManager;
import dev.allround.cloud.servicegroup.ServiceGroup;

public class TestGroupCommand implements ICommand {
    @Override
    public boolean onExecute(ICommandSender sender, String command, String[] args) {
        IServiceGroupManager serviceGroupManager = Cloud.getModule().getComponent(IServiceGroupManager.class);
        IServiceGroup serviceGroup = serviceGroupManager.getServiceGroup("TestGroup").get();
        sender.sendMessage("Min. Service Amount hochgesetzt: "+serviceGroup.getMinOnlineAmount()+" -> "+(serviceGroup.getMinOnlineAmount() + 1));
        sender.sendMessage("Max. Service Amount hochgesetzt: "+serviceGroup.getMaxOnlineAmount()+" -> "+(serviceGroup.getMaxOnlineAmount() + 1));
        serviceGroup.setMinOnlineAmount(serviceGroup.getMinOnlineAmount() + 1);
        serviceGroup.setMaxOnlineAmount(serviceGroup.getMaxOnlineAmount() + 1);
        return true;
    }
}
