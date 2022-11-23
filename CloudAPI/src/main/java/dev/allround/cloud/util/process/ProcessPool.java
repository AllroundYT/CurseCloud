package dev.allround.cloud.util.process;

import dev.allround.cloud.Cloud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ProcessPool {
    private final HashMap<ProcessBuilder, List<ManageableProcess>> processList;

    public ProcessPool() {
        this.processList = new HashMap<>();
    }

    public HashMap<ProcessBuilder, List<ManageableProcess>> getProcessList() {
        return processList;
    }

    public long startProcess(ProcessBuilder processBuilder) throws IOException {
        Process process = processBuilder.start();
        ManageableProcess manageableProcess = new ManageableProcess(process, Cloud.getModule().getCloudLogger()::raw);
        this.processList.compute(processBuilder, (processBuilder1, manageableProcesses) -> {
            if (manageableProcesses == null) manageableProcesses = new ArrayList<>();
            manageableProcesses.add(manageableProcess);
            return manageableProcesses;
        });

        return process.pid();
    }

    public long startProcess(ProcessBuilder processBuilder, Consumer<String> onLogLine) throws IOException {
        Process process = processBuilder.start();
        ManageableProcess manageableProcess = new ManageableProcess(process, onLogLine);
        this.processList.compute(processBuilder, (processBuilder1, manageableProcesses) -> {
            if (manageableProcesses == null) manageableProcesses = new ArrayList<>();
            manageableProcesses.add(manageableProcess);
            return manageableProcesses;
        });

        return process.pid();
    }

    public ManageableProcess getProcess(long pid) {
        return this.processList.values().stream().filter(manageableProcesses -> manageableProcesses.stream().anyMatch(manageableProcess -> manageableProcess.getProcess().pid() == pid)).findFirst().get().stream().filter(manageableProcess -> manageableProcess.getProcess().pid() == pid).findFirst().get();
    }

    public boolean stopProcess(Process process) {
        return getProcess(process.pid()).stop();

    }

    public boolean stopProcess(long pid) {
        return getProcess(pid).stop();

    }

    public void stopAll(ProcessBuilder processBuilder) {
        this.processList.getOrDefault(processBuilder, new ArrayList<>()).forEach(ManageableProcess::stop);
    }
}
