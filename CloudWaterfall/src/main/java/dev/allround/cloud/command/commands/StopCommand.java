package dev.allround.cloud.command.commands;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleInfo;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.PacketType;

import java.time.Duration;
import java.time.Instant;

public class StopCommand implements ICommand {
    private Instant lastAttempt = Instant.now();
    @Override
    public boolean onExecute(ICommandSender sender, String command, String[] args) {
        if (Duration.between(lastAttempt, Instant.now()).toMillis() < 10000 & args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(PacketType.API_STOP_CLOUD,new String[]{new Gson().toJson(Cloud.getWrapper().getThisModule())},response -> {
                if (response.getData()[0].equalsIgnoreCase("SUCCESS")){
                    sender.sendMessage("§aCloud is stopping...");
                }
            });
        }else {
            sender.sendMessage("§cPlease use \"/cloud stop confirm\" in the next 10 seconds to confirm stopping the cloud.");
        }
        return true;
    }
}
