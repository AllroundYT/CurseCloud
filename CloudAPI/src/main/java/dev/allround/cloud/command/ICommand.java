package dev.allround.cloud.command;

public interface ICommand {
    void onExecute(ICommandSender sender, String command, String[] args);
}
