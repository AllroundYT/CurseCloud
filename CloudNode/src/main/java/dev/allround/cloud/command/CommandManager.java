package dev.allround.cloud.command;

import dev.allround.cloud.command.commands.*;
import dev.allround.cloud.command.premade.*;

public class CommandManager extends ICommandManager {
    @Override
    public void init() {
        registerCommand(new TestGroupCommand(),"testgroup","","cloud.command.testgroup","tg");
        //registerCommand(new ListProxies(),"Listproxies","Lists all proxies","cloud.command.listproxies","lproxies");
    }
}
