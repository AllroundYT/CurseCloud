package dev.allround.cloud.command;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.setup.ISetupManager;

public class InputManager extends IInputManager {
    @Override
    public void onInput(String line) {
        if (Cloud.getModule().getComponent(ISetupManager.class).isSetupRunning()){
            Cloud.getModule().getComponent(ISetupManager.class).parseLine(line);
            return;
        }
        Cloud.getModule().getComponent(ICommandManager.class).parseLine(line, new ConsoleSender());
    }
}
