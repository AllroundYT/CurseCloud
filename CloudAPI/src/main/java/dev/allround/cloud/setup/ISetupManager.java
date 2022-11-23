package dev.allround.cloud.setup;

import java.util.List;
import java.util.Optional;

public interface ISetupManager {
    void startSetup(Setup setup);

    Optional<Setup> getRunningSetup();

    List<Setup> getWaitingSetups();

    default boolean isSetupRunning() {
        return getRunningSetup().isPresent();
    }

    default void parseLine(String line) {
        if (!isSetupRunning()) return;
        getRunningSetup().get().onInput(line);
    }
}
