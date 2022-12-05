package dev.allround.cloud.command.premade;

import dev.allround.cloud.command.ICommand;
import dev.allround.cloud.command.ICommandSender;

import java.time.Duration;
import java.time.Instant;

public class StopCommand implements ICommand {
    private Instant lastAttempt = Instant.now();

    @Override
    public void onExecute(ICommandSender sender, String command, String[] args) {
        if (Duration.between(lastAttempt, Instant.now()).toMillis() < 10000) {
            System.exit(0);
        } else {
            sender.sendMessage("Please enter this command again in the next 10 seconds to stop this node.");
            this.lastAttempt = Instant.now();
        }
    }
}
