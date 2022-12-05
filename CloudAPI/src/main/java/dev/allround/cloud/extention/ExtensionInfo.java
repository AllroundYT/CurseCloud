package dev.allround.cloud.extention;

import lombok.Getter;

@Getter
public class ExtensionInfo {
    private final String name, mainClass, version, author, description,apiVersion;
    private CloudExtension cloudExtension;
    private boolean loaded, enabled;

    public ExtensionInfo(String name, String mainClass, String version, String author, String description, String apiVersion) {
        this.name = name;
        this.mainClass = mainClass;
        this.version = version;
        this.author = author;
        this.description = description;
        this.apiVersion = apiVersion;
        this.loaded = false;
        this.enabled = false;
    }

    public ExtensionInfo setCloudExtension(CloudExtension cloudExtension) {
        this.cloudExtension = cloudExtension;
        return this;
    }


    public ExtensionInfo setLoaded(boolean loaded) {
        this.loaded = loaded;
        return this;
    }

    public ExtensionInfo setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
