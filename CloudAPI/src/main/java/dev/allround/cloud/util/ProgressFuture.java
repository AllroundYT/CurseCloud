package dev.allround.cloud.util;

public class ProgressFuture {
    private final Runnable onProgressDone;
    private final int progressTarget;
    private int progress;
    private boolean done;

    public ProgressFuture(Runnable onProgressDone, int progressTarget) {
        this.onProgressDone = onProgressDone;
        this.progressTarget = progressTarget;
        this.progress = 0;
        this.done = false;
    }

    public int getProgress() {
        return progress;
    }

    public ProgressFuture setProgress(int progress) {
        this.progress = progress;
        checkForDone();
        return this;
    }

    public void addProgress(int i) {
        this.progress += i;
        checkForDone();
    }

    public void checkForDone() {
        if (this.done) return;
        if (!isDone()) return;
        this.onProgressDone.run();
        this.done = true;
    }

    public int getProgressTarget() {
        return progressTarget;
    }

    public Runnable getOnProgressDone() {
        return onProgressDone;
    }

    public boolean isDone() {
        return this.progress >= this.progressTarget;
    }

    public ProgressFuture setDone(boolean done) {
        this.done = done;
        checkForDone();
        return this;
    }
}
