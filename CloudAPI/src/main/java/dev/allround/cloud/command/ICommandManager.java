package dev.allround.cloud.command;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.command.premade.*;
import dev.allround.cloud.util.Initializeable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ICommandManager implements Initializeable {
    private final List<RegisteredCommand> registeredCommands;

    public ICommandManager() {
        this.registeredCommands = new ArrayList<>();
        registerDefaultCommands();
    }

    public void registerCommand(ICommand command, String name, String description, String permission, String... alias) {
        RegisteredCommand registeredCommand = new RegisteredCommand(command, name, description, permission, alias);
        this.registeredCommands.add(registeredCommand);
        Cloud.getModule().getCloudLogger().debug("[CommandManager] Command registered -> Command: " + registeredCommand);
    }

    public void parseLine(String line, ICommandSender sender) {
        String[] strings = line.split(" ");
        String cmd = strings[0];
        String[] args = Arrays.stream(strings).toList().subList(1, strings.length).toArray(new String[0]);

        if (getCommand(cmd.trim()).isEmpty()) {
            Cloud.getModule().getCloudLogger().warn("No command found with this name. Try help for info.");
        } else {
            RegisteredCommand command = getCommand(cmd.trim()).get();
            if (hasPermission(sender, command.permission)) {
                command.command().onExecute(sender, cmd, args);
            } else sender.sendMessage("You don't have the needed permission to perform this command.");
        }
    }

    public List<RegisteredCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    public Optional<RegisteredCommand> getCommand(String name) {
        return this.registeredCommands.stream().filter(registeredCommand -> registeredCommand.name.equalsIgnoreCase(name) || Arrays.stream(registeredCommand.alias).anyMatch(s -> s.equalsIgnoreCase(name))).findFirst();
    }

    public void registerDefaultCommands() {
        registerCommand(new HelpCommand(), "Help", "Help displays all commands.", "", "?");
        registerCommand(new InfoCommand(), "Information", "Displays information about the cloud.", "", "Info");
        registerCommand(new ListModulesCommand(), "ListSockets", "Displays a list with all connected sockets.", "cloud.command.listsockets", "lns");
        registerCommand(new AddGroupCommand(), "AddGroup", "Adds a new group", "cloud.command.addgroup", "agroup", "creategroup");
        registerCommand(new ListGroupsCommand(), "listgroups", "", "", "lg");
        registerCommand(new ListServicesCommand(), "listServices", "", "", "ls");
        registerCommand(new ServiceInfoCommand(), "serviceInfo", "", "", "si", "serinfo");
        registerCommand(new GroupInfoCommand(), "groupinfo", "", "", "gi", "ginfo");
    }

    private boolean hasPermission(ICommandSender sender, String perm) {
        return sender.hasPermission(perm.trim());
    }

    public record RegisteredCommand(ICommand command, String name, String description, String permission,
                                    String... alias) {
        @Override
        public String toString() {
            return "{" + "command=" + command + ", name=" + name + ", description=" + description + ", permission=" + permission + ", alias=" + Arrays.toString(alias) + '}';
        }
    }
}
