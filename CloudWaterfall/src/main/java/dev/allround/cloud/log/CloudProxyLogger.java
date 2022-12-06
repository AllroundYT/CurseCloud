package dev.allround.cloud.log;

import net.md_5.bungee.api.ProxyServer;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CloudProxyLogger implements Logger {
    private final LinkedList<LogEntry> logEntries;
    private boolean debugMode;

    public CloudProxyLogger() {
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
    public void info(Object... objects) {
        List.of(objects).forEach(o -> {
            ProxyServer.getInstance().getLogger().info(o.toString());
        });
    }

    @Override
    public void warn(Object... objects) {
        List.of(objects).forEach(o -> {
            ProxyServer.getInstance().getLogger().warning(o.toString());
        });
    }

    @Override
    public void error(Object... objects) {
        List.of(objects).forEach(o -> {
            ProxyServer.getInstance().getLogger().severe(o.toString());
        });
    }

    @Override
    public void silentError(Object... objects) {
        List.of(objects).forEach(o -> {
            ProxyServer.getInstance().getLogger().severe(o.toString());
        });
    }

    @Override
    public void debug(Object... objects) {
        List.of(objects).forEach(o -> {
            ProxyServer.getInstance().getLogger().config(o.toString());
        });
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
    public LinkedList<LogEntry> getLogEntries() {
        return logEntries;
    }

    @Override
    public void saveToFile() {
    }
}
