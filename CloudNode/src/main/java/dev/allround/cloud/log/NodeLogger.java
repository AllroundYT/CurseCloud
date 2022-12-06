package dev.allround.cloud.log;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.util.FileUtils;
import dev.allround.cloud.util.Stopable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class NodeLogger implements Logger, Stopable {
    private final LinkedList<LogEntry> logEntries;
    private boolean debugMode;

    public NodeLogger() {
        this.logEntries = new LinkedList<>();
        this.debugMode = Boolean.getBoolean("cloud.log.debug");
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = (debugMode || Boolean.getBoolean("cloud.log.debug"));
    }

    @Override
    public String format(Level level, String prefix, String line) {
        StringBuilder stringBuilder = new StringBuilder();

        Calendar.Builder builder = new Calendar.Builder();
        builder.setInstant(Date.from(Instant.now()));
        Calendar calendar = builder.build();


        stringBuilder.append("[").append(calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE)).append(":").append(calendar.get(Calendar.SECOND) < 10 ? "0" + calendar.get(Calendar.SECOND) : calendar.get(Calendar.SECOND)).append("|").append(level.name()).append("] -> ").append(line);

        return stringBuilder.toString();
    }

    @Override
    public void raw(Object... objects) {
        for (Object o : objects) {
            if (o instanceof Throwable throwable) {
                raw(throwable.getMessage());
                raw((Object) throwable.getStackTrace());
                raw(throwable.getCause());
                raw((Object) throwable.getSuppressed());
            } else {
                String line = String.valueOf(o);
                System.out.println(line);
                this.logEntries.add(new LogEntry(line, Level.RAW));
            }
        }
    }

    @Override
    public void info(Object... objects) {
        log(Level.INFO, true, objects);
    }

    @Override
    public void warn(Object... objects) {
        log(Level.WARN, true, objects);
    }

    @Override
    public void error(Object... objects) {
        log(Level.ERROR, true, objects);
    }

    @Override
    public void silentError(Object... objects) {
        log(Level.ERROR, false, objects);
    }

    @Override
    public void debug(Object... objects) {
        log(Level.DEBUG, debugMode, objects);
    }


    private void log(Level level, boolean print, Object... objects) {
        for (Object o : objects) {
            if (o instanceof Throwable throwable) {
                log(level, print, throwable.getMessage());
                log(level, print, throwable.getLocalizedMessage());
                log(level, print, throwable.getStackTrace());
                log(level, print, throwable.getCause());
                log(level, print, throwable.getSuppressed());
            } else {
                String line = format(level, Cloud.getModule().getModuleInfo().name(), String.valueOf(o));
                if (print) System.out.println(line);
                this.logEntries.add(new LogEntry(line, level));
            }
        }
    }

    @Override
    public List<LogEntry> getLogEntries() {
        return this.logEntries;
    }

    @Override
    public void saveToFile() {

        try {
            for (String logs : Objects.requireNonNull(FileUtils.getFile(Path.of("logs")).toFile().list((dir, name) -> name.equalsIgnoreCase(".log")))) {
                Path logFile = Path.of("logs", logs);
                if (TimeUnit.MILLISECONDS.toDays(logFile.toFile().lastModified()) > 3) {
                    logFile.toFile().delete();
                }
            }
        } catch (Exception e) {
            error(e);
        }

        File infoLogFile = FileUtils.getFile(Path.of("logs", System.currentTimeMillis() + ".info.log")).toFile();
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(infoLogFile))) {
            logEntries.stream().filter(logEntry -> logEntry.level() == Level.INFO).forEach(logEntry -> {
                try {
                    bufferedOutputStream.write((logEntry.text() + "\n").getBytes());
                } catch (IOException e) {
                    error(e);
                }
            });
            bufferedOutputStream.flush();
        } catch (IOException e) {
            error(e);
        }


        File warnLogFile = FileUtils.getFile(Path.of("logs", System.currentTimeMillis() + ".warn.log")).toFile();
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(warnLogFile))) {
            logEntries.stream().filter(logEntry -> logEntry.level() == Level.WARN).forEach(logEntry -> {
                try {
                    bufferedOutputStream.write((logEntry.text() + "\n").getBytes());
                } catch (IOException e) {
                    error(e);
                }
            });
            bufferedOutputStream.flush();
        } catch (IOException e) {
            error(e);
        }

        File errorLogFile = FileUtils.getFile(Path.of("logs", System.currentTimeMillis() + ".error.log")).toFile();
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(errorLogFile))) {
            logEntries.stream().filter(logEntry -> logEntry.level() == Level.ERROR).forEach(logEntry -> {
                try {
                    bufferedOutputStream.write((logEntry.text() + "\n").getBytes());
                } catch (IOException e) {
                    error(e);
                }
            });
            bufferedOutputStream.flush();
        } catch (IOException e) {
            error(e);
        }

        File debugLogFile = FileUtils.getFile(Path.of("logs", System.currentTimeMillis() + ".debug.log")).toFile();
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(debugLogFile))) {
            logEntries.stream().filter(logEntry -> logEntry.level() == Level.DEBUG).forEach(logEntry -> {
                try {
                    bufferedOutputStream.write((logEntry.text() + "\n").getBytes());
                } catch (IOException e) {
                    error(e);
                }
            });
            bufferedOutputStream.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    @Override
    public void stop() {
        saveToFile();
    }
}
