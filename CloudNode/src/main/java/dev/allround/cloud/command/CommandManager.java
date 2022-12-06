package dev.allround.cloud.command;

import dev.allround.cloud.command.commands.RestartCommand;
import dev.allround.cloud.command.commands.StopCommand;
import dev.allround.cloud.command.commands.TestGroupCommand;

public class CommandManager extends ICommandManager {
    @Override
    public void init() {
        registerCommand(new TestGroupCommand(), "testgroup", "", "cloud.command.testgroup", "tg");
        registerCommand(new StopCommand(), "StopNode", "Stops this node.", "cloud.command.stopnode", "stopn", "stop");
        registerCommand(new RestartCommand(), "RestartNode", "Restarts this node", "cloud.command.restartnode", "restartn", "restart");
        //registerCommand(new ListProxies(),"Listproxies","Lists all proxies","cloud.command.listproxies","lproxies");
    }
}
