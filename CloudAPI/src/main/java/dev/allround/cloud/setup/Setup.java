package dev.allround.cloud.setup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;

import java.util.function.BiConsumer;

public class Setup implements Startable, Stopable { //TODO: Setup system fertig stellen
    public final SetupPart[] setupParts;
    private int activePart;
    private boolean done;
    private boolean started;
    private final String onDoneMsg;

    public Setup(SetupPart[] setupParts, String onDoneMsg) {
        this.setupParts = setupParts;
        this.onDoneMsg = onDoneMsg;
    }

    public boolean isDone() {
        return done;
    }

    public SetupPart getActivePart() {
        return setupParts[this.activePart];
    }

    public void startNextPart() {
        if (checkForDone()) {
            Cloud.getModule().getCloudLogger().info(this.onDoneMsg);
            return;
        }
        if (!setupParts[activePart].isDone()) return;
        activePart++;
        Cloud.getModule().getCloudLogger().info(setupParts[activePart].infoText);
    }

    public void onInput(String line){
        if (line.trim().equalsIgnoreCase("") || line.trim().equalsIgnoreCase(" ")){
            setupParts[activePart].getOnInput().accept(setupParts[activePart].getDefaultValue(),this);
        }else {
            setupParts[activePart].getOnInput().accept(line,this);
        }
        startNextPart();
    }

    public boolean checkForDone() {
        if (this.activePart >= this.setupParts.length) {
            this.done = true;
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        if (started) return;
        this.started = true;
        startNextPart();
    }

    @Override
    public void stop() {
        if (!started) return;
        this.done = true;
        this.started = false;
    }

    public static class SetupPart {
        private final BiConsumer<String, Setup> onInput;
        private final String infoText;
        private final String errorText;
        private final String defaultValue;
        private final String doneText;
        private boolean done;

        public SetupPart(BiConsumer<String, Setup> onInput, String infoText, String errorText, String doneText, String defaultValue) {
            this.onInput = onInput;
            this.infoText = infoText;
            this.errorText = errorText;
            this.defaultValue = defaultValue;
            this.doneText = doneText;
            this.done = false;
        }

        public boolean isDone() {
            return done;
        }

        public SetupPart setDone(boolean done) {
            this.done = done;
            return this;
        }

        public BiConsumer<String, Setup> getOnInput() {
            return onInput;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getDoneText() {
            return doneText;
        }

        public String getErrorText() {
            return errorText;
        }

        public String getInfoText() {
            return infoText;
        }
    }
}
