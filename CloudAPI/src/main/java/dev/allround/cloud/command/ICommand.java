package dev.allround.cloud.command;

public interface ICommand {
    boolean onExecute(ICommandSender sender, String command, String[] args);
}
