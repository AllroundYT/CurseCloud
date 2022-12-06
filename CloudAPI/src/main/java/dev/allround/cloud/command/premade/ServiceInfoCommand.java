package dev.allround.cloud.command.premade;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.service.IService;
import dev.allround.cloud.service.IServiceManager;

public class ServiceInfoCommand implements ICommand {
    @Override
    public boolean onExecute(ICommandSender sender, String command, String[] args) {
        if (args.length == 1){
            String serviceID = args[0];

            IServiceManager serviceManager = Cloud.getModule().getComponent(IServiceManager.class);

            if (serviceManager.getService(serviceID).isEmpty()){
                sender.sendMessage("There is no service with this id.");
                return false;
            }

            IService iService = serviceManager.getService(serviceID).get();

            sender.sendMessage("");
            sender.sendMessage("----------- Service Info: -----------");
            sender.sendMessage("Service ID -> "+serviceID);
            sender.sendMessage("Service group -> "+iService.getServiceGroup());
            sender.sendMessage("Node -> "+iService.getNode());
            sender.sendMessage("Service type -> "+iService.getType());
            sender.sendMessage("Service version -> "+iService.getServiceVersion());
            sender.sendMessage("Status -> "+iService.getStatus());
            sender.sendMessage("Start arguments -> "+iService.getStartArgs());
            sender.sendMessage("Java parameters -> "+iService.getJavaParams());
            sender.sendMessage("Adresse -> "+iService.getSocketAddress().host()+":"+iService.getSocketAddress().port());
            sender.sendMessage("Online players -> "+iService.getPlayers().size());
            sender.sendMessage("Max. online players -> "+iService.getMaxPlayers());
            sender.sendMessage("MOTD line 1 -> "+iService.getMotd()[0]);
            sender.sendMessage("MOTD line 2 -> "+iService.getMotd()[1]);
            sender.sendMessage("Max. RAM -> "+iService.getMaxRam());
            sender.sendMessage("-------------------------------------");
            sender.sendMessage("");
        }else {
            sender.sendMessage("Please use \"serviceinfo <Service-ID>\"");
        }
        return true;
    }
}
