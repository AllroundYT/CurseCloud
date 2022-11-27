package dev.allround.cloud.command;

import dev.allround.cloud.Cloud;

public class InputManager extends IInputManager {
    @Override
    public void onInput(String line) {
        Cloud.getModule().getComponent(ICommandManager.class).parseLine(line, new ConsoleSender());
    }
}
