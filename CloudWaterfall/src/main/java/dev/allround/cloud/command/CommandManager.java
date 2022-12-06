package dev.allround.cloud.command;

import dev.allround.cloud.command.commands.StopCommand;

public class CommandManager extends ICommandManager {
    @Override
    public void init() {
        registerCommand(new StopCommand(), "StopCloud", "Stops the whole cloud.", "cloud.command.stop");
    }
}
