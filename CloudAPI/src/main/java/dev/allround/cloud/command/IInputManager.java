package dev.allround.cloud.command;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public abstract class IInputManager extends BufferedReader implements Startable, Stopable {

    private final Scanner inputScanner;
    private boolean isRunning;

    public IInputManager() {
        super(new InputStreamReader(System.in));
        this.inputScanner = new Scanner(System.in);


    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public void start() {
        this.isRunning = true;
        Cloud.getModule().getCachedThreadPool().submit(() -> {
            while (isRunning) {
                try {
                    onInput(readLine());
                    //System.out.print("> ");
                }catch (Exception e){
                    Cloud.getModule().getCloudLogger().error(e);
                }
            }
        });
    }

    public Scanner getInputScanner() {
        return inputScanner;
    }

    public abstract void onInput(String line);
}
