package dev.allround.cloud.command.commands;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.service.ServiceVersion;
import dev.allround.cloud.servicegroup.IServiceGroupManager;
import dev.allround.cloud.servicegroup.ServiceGroup;

public class AddGroupCommand implements ICommand {
    @Override
    public void onExecute(ICommandSender sender, String command, String[] args) {
        if (args.length == 7 && args[0].equalsIgnoreCase("service")){
            String name = args[1];
            String maxRamString = args[2];
            String maxPlayersString = args[3];
            String minOnlineAmountString = args[4];
            String maxOnlineAmountString = args[5];
            String serviceVersionString = args[6];

            int minOnlineAmount = 1;
            int maxOnlineAmount = 1;
            int maxPlayers = 100;
            int maxRam = 1024;
            ServiceVersion serviceVersion = ServiceVersion.SPIGOT_1_18_2;
            try {
                maxPlayers = Integer.parseInt(maxPlayersString);
            }catch (NumberFormatException ignored){}
            try {
                maxRam = Integer.parseInt(maxRamString);
            }catch (NumberFormatException ignored){}
            try {
                minOnlineAmount = Integer.parseInt(minOnlineAmountString);
            }catch (NumberFormatException ignored){}
            try {
                maxOnlineAmount = Integer.parseInt(maxOnlineAmountString);
            }catch (NumberFormatException ignored){}
            try {
                serviceVersion = ServiceVersion.valueOf(serviceVersionString);
            }catch (IllegalArgumentException ignored){}

            String node = Cloud.getModule().getComponent(IServiceGroupManager.class).getNodeWithMostLessGroups();

            ServiceGroup serviceGroup = new ServiceGroup(node,maxRam,maxPlayers,name,minOnlineAmount,maxOnlineAmount,0.9, serviceVersion);

            Cloud.getModule().getComponent(IServiceGroupManager.class).registerServiceGroup(serviceGroup);
        }/*else if (args.length == 6 && args[0].equalsIgnoreCase("proxy")){
            String name = args[1];
            String maxRamString = args[2];
            String maxPlayersString = args[3];
            String minOnlineAmountString = args[4];
            String maxOnlineAmountString = args[5];

            int minOnlineAmount = 1;
            int maxOnlineAmount = 1;
            int maxPlayers = 100;
            int maxRam = 512;
            try {
                maxPlayers = Integer.parseInt(maxPlayersString);
            }catch (NumberFormatException ignored){}
            try {
                maxRam = Integer.parseInt(maxRamString);
            }catch (NumberFormatException ignored){}
            try {
                minOnlineAmount = Integer.parseInt(minOnlineAmountString);
            }catch (NumberFormatException ignored){}
            try {
                maxOnlineAmount = Integer.parseInt(maxOnlineAmountString);
            }catch (NumberFormatException ignored){}

            String node = Cloud.getWrapper().getThisModule().name();

            Proxy proxy = new Proxy(
                    "-Dcloud.network.port=%PORT% -Dcloud.network.host=%HOST%",
                    SocketAddress.inetSocketAddress(PortChecker.getFreePort(20000,30000,25565), "127.0.0.1"),
                    maxRam,
                    new String[]{"§bAllroundCloud Proxy","§7by Allround | Julian"},
                    maxPlayers,
                    false,
                    node);

            sender.sendMessage("Proxy created -> "+proxy.getProxyId());

            Cloud.getModule().getComponent(IProxyManager.class).queueStart(proxy);
        }
        */
        else {
            sendHelpMessage(sender);
        }
    }

    private void sendHelpMessage(ICommandSender commandSender){
        commandSender.sendMessage("Please use this command like this:");
        commandSender.sendMessage("addgroup <PROXY:SERVICE> <Name (String)> <max. Ram in MegaByte (int)> <max. Players (int)> <MinOnlineAmount (int)> <MaxOnlineAmount (int)> <Service version (only for Services; double)>");
    }
}
