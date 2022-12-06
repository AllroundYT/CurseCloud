package dev.allround.cloud.command;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;
import jline.console.ConsoleReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class IInputManager extends BufferedReader implements Startable, Stopable {

    private final boolean useJLine;
    private boolean isRunning;

    public IInputManager(boolean useJLine) {
        super(new InputStreamReader(System.in));
        this.useJLine = useJLine;


    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public void start() {
        this.isRunning = true;
        Cloud.getModule().getCachedThreadPool().submit(() -> {
            if (useJLine) {

                try {
                    ConsoleReader consoleReader = new ConsoleReader();
                    consoleReader.setPrompt("-> ");
                    while (isRunning) {
                        System.out.println(consoleReader.accept());
                    }
                } catch (IOException e) {
                    Cloud.getModule().getCloudLogger().error(e);
                }
            } else {
                while (isRunning) {
                    try {
                        onInput(readLine());
                        //System.out.print("> ");
                    } catch (Exception e) {
                        Cloud.getModule().getCloudLogger().error(e);
                    }
                }
            }
        });
    }


    public abstract void onInput(String line);
}
