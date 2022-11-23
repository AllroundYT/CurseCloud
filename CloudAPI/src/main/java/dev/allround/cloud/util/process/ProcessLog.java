package dev.allround.cloud.util.process;

import dev.allround.cloud.Cloud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class ProcessLog {
    private final Process process;
    private final LinkedList<String> logLines;
    private final Consumer<String> onActive;
    private boolean active;

    public ProcessLog(Process process, Consumer<String> onActive) {
        this.process = process;
        this.onActive = onActive;
        this.logLines = new LinkedList<>();

        Cloud.getModule().getCachedThreadPool().submit(() -> {
            Scanner logScanner = new Scanner(this.process.getInputStream());
            Scanner errScanner = new Scanner(this.process.getErrorStream());
            while (this.process.isAlive()) {
                if (logScanner.hasNextLine()) {
                    this.logLines.add(logScanner.nextLine());
                }
                if (errScanner.hasNextLine()) {
                    this.logLines.add(errScanner.nextLine());
                }
            }
        });
    }

    public ProcessLog(Process process, Consumer<String> onLogLine, Consumer<String> onActive) {
        this.process = process;
        this.onActive = onActive;
        this.logLines = new LinkedList<>();

        Cloud.getModule().getCachedThreadPool().submit(() -> {
            Scanner logScanner = new Scanner(this.process.getInputStream());
            Scanner errScanner = new Scanner(this.process.getErrorStream());
            while (this.process.isAlive()) {
                if (logScanner.hasNextLine()) {
                    onLogLine.accept(logScanner.nextLine());
                }
                if (errScanner.hasNextLine()) {
                    onLogLine.accept(errScanner.nextLine());
                }
            }
        });
    }

    public boolean isActive() {
        return active;
    }

    public ProcessLog setActive(boolean active) {
        this.active = active;
        return this;
    }

    public void input(String line) throws IOException {
        this.process.getOutputStream().write(line.getBytes());
        this.process.getOutputStream().flush();
    }

    public void readLines(int amount) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < (this.logLines.size()) && i < amount; i++) {
            out.add(this.logLines.get((this.logLines.size() - 1) - i));
        }
    }

    public List<String> readAllLines() {
        return new ArrayList<>(this.logLines);
    }

    public void clear() {
        this.logLines.clear();
    }

    public Process getProcess() {
        return process;
    }

    public LinkedList<String> getLogLines() {
        return logLines;
    }
}
