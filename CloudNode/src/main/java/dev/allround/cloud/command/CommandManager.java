package dev.allround.cloud.command;

import dev.allround.cloud.command.commands.AddGroupCommand;
import dev.allround.cloud.command.commands.ListModulesCommand;
import dev.allround.cloud.command.commands.RestartCommand;
import dev.allround.cloud.command.commands.StopCommand;

public class CommandManager extends ICommandManager {
    @Override
    public void init() {
        registerCommand(new ListModulesCommand(), "ListSockets", "Displays a list with all connected sockets.", "cloud.command.listsockets", "lns");
        registerCommand(new StopCommand(), "StopNode", "Stops this node.", "cloud.command.stopnode", "stopn", "stop");
        registerCommand(new RestartCommand(),"RestartNode","Restarts this node","cloud.command.restartnode","restartn","restart");
        registerCommand(new AddGroupCommand(),"AddGroup","Adds a new group","cloud.command.addgroup","agroup","creategroup");
        //registerCommand(new ListProxies(),"Listproxies","Lists all proxies","cloud.command.listproxies","lproxies");
    }
}
