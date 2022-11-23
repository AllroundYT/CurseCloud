package dev.allround.cloud.command.premade;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandManager;
import dev.allround.cloud.command.ICommandSender;

import java.util.Arrays;

public class HelpCommand implements ICommand {
    @Override
    public void onExecute(ICommandSender sender, String command, String[] args) {
        sender.sendMessage("Command List:");
        Cloud.getModule().getComponent(ICommandManager.class).getRegisteredCommands().forEach(registeredCommand -> {
            sender.sendMessage(" Command: " + registeredCommand.name() + " - Description: " + registeredCommand.description() + " - Permission: " + registeredCommand.permission() + " - Aliases: " + Arrays.toString(registeredCommand.alias()));
        });
    }
}
