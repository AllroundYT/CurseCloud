package dev.allround.cloud.command.premade;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.servicegroup.IServiceGroup;
import dev.allround.cloud.servicegroup.IServiceGroupManager;

public class GroupInfoCommand implements ICommand {
    @Override
    public boolean onExecute(ICommandSender sender, String command, String[] args) {
        if (args.length == 1) {
            String groupName = args[0];

            IServiceGroupManager groupManager = Cloud.getModule().getComponent(IServiceGroupManager.class);

            if (groupManager.getServiceGroup(groupName).isEmpty()) {
                sender.sendMessage("There is no service group with this name.");
                return false;
            }

            IServiceGroup iServiceGroup = groupManager.getServiceGroup(groupName).get();

            sender.sendMessage("");
            sender.sendMessage("----------- Service Group Info: ------------");
            sender.sendMessage("Group name -> " + iServiceGroup.getGroupName());
            sender.sendMessage("Node -> " + iServiceGroup.getNode());
            sender.sendMessage("Service Type -> " + iServiceGroup.getType());
            sender.sendMessage("Service version -> " + iServiceGroup.getServiceVersion());
            sender.sendMessage("Services:");
            iServiceGroup.getServices().forEach(service -> {
                sender.sendMessage("  - " + service.getServiceID());
            });
            sender.sendMessage("Java parameters -> " + iServiceGroup.getJavaParams());
            sender.sendMessage("Start arguments -> " + iServiceGroup.getStartArgs());
            sender.sendMessage("Online Service amount -> " + iServiceGroup.getOnlineServiceAmount());
            sender.sendMessage("Min. online Service amount -> " + iServiceGroup.getMinOnlineAmount());
            sender.sendMessage("Max. online Service amount -> " + iServiceGroup.getMaxOnlineAmount());
            sender.sendMessage("Percentage to start new service -> " + iServiceGroup.getPercentageToStartNewService());
            sender.sendMessage("Online players -> " + iServiceGroup.getPlayers().size());
            sender.sendMessage("Max. players -> " + iServiceGroup.getMaxPlayers());
            sender.sendMessage("Max. RAM -> " + iServiceGroup.getMaxRam());
            sender.sendMessage("--------------------------------------------");
            sender.sendMessage("");
        } else {
            sender.sendMessage("Please use \"groupinfo <Group>\"");
        }
        return true;
    }
}
