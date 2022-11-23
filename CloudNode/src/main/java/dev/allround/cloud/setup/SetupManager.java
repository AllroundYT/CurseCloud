package dev.allround.cloud.setup;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SetupManager implements ISetupManager {
    private final LinkedList<Setup> waitingSetups;
    private Setup runningSetup;

    public SetupManager() {
        this.waitingSetups = new LinkedList<>();
    }

    @Override
    public void startSetup(Setup setup) {
        if (isSetupRunning()) this.waitingSetups.add(setup);
        else this.runningSetup = setup;
    }

    public void update() {
        if (!isSetupRunning()) return;
        if (runningSetup.isDone()) {
            if (waitingSetups.size() == 0) return;
            this.runningSetup = this.waitingSetups.remove();
        }
        this.runningSetup.start();
    }

    @Override
    public Optional<Setup> getRunningSetup() {
        return Optional.ofNullable(this.runningSetup);
    }

    @Override
    public List<Setup> getWaitingSetups() {
        return waitingSetups;
    }
}
