package dev.allround.cloud.log;

import java.util.List;

public interface Logger {

    String format(Level level, String prefix, String line);

    default String getCloudLogo() {
        return """
                 _______ ___     ___     ______   _______ __   __ __    _ ______  _______ ___     _______ __   __ ______
                |   _   |   |   |   |   |    _ | |       |  | |  |  |  | |      ||       |   |   |       |  | |  |      |
                |  |_|  |   |   |   |   |   | || |   _   |  | |  |   |_| |  _    |       |   |   |   _   |  | |  |  _    |
                |       |   |   |   |   |   |_||_|  | |  |  |_|  |       | | |   |       |   |   |  | |  |  |_|  | | |   |
                |       |   |___|   |___|    __  |  |_|  |       |  _    | |_|   |      _|   |___|  |_|  |       | |_|   |
                |   _   |       |       |   |  | |       |       | | |   |       |     |_|       |       |       |       |
                |__| |__|_______|_______|___|  |_|_______|_______|_|  |__|______||_______|_______|_______|_______|______|
                """;
    }

    void info(Object... objects);

    void warn(Object... objects);

    void error(Object... objects);

    void silentError(Object... objects);

    void debug(Object... objects);

    void raw(Object... objects);

    List<LogEntry> getLogEntries();

    void saveToFile();

    enum Level {
        INFO, WARN, ERROR, DEBUG, RAW,
    }

    record LogEntry(String text, Level level) {
    }
}
