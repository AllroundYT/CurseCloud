package dev.allround.cloud.command.commands;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;

import java.time.Duration;
import java.time.Instant;

public class RestartCommand implements ICommand {
    private Instant lastAttempt = Instant.now();

    @Override
    public boolean onExecute(ICommandSender sender, String command, String[] args) {
        if (Duration.between(lastAttempt, Instant.now()).toMillis() < 10000) {
            Cloud.getModule().stop();
            Cloud.getModule().init();
            Cloud.getModule().start();
        } else {
            sender.sendMessage("Please enter this command again in the next 10 seconds to restart this node.");
            this.lastAttempt = Instant.now();
        }
        return true;
    }
}
