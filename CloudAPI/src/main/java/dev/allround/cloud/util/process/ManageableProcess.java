package dev.allround.cloud.util.process;

import java.util.function.Consumer;

public class ManageableProcess {
    private final Process process;
    private final ProcessLog processLog;

    public ManageableProcess(Process process, Consumer<String> onActive) {
        this.process = process;
        this.processLog = new ProcessLog(process, onActive);
    }

    public ManageableProcess(Process process, Consumer<String> onLogLine, Consumer<String> onActive) {
        this.process = process;
        this.processLog = new ProcessLog(process, onLogLine);
    }

    public boolean stop() {
        if (this.process != null && this.process.isAlive()) {
            this.process.destroy();
            return true;
        }
        return false;
    }

    public Process getProcess() {
        return process;
    }

    public ProcessLog getProcessLog() {
        return processLog;
    }
}
