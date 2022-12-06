package dev.allround.cloud;

import java.util.UUID;

public class ModuleInfo {
    private final String name;
    private final String version;
    private final UUID id;
    private final String type;

    public ModuleInfo(String name, String version, UUID id, ModuleType moduleType) {
        this.name = name;
        this.version = version;
        this.id = id;
        this.type = moduleType.name();
    }

    public ModuleType getType() {
        return ModuleType.valueOf(this.type);
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String version() {
        return version;
    }

    @Override
    public String toString() {
        return "ModuleInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
