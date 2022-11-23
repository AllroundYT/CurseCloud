package dev.allround.cloud.extention;

public class ExtensionInfo {
    private final String name, mainClass, version, author, description;
    private CloudExtension cloudExtension;
    private boolean loaded, enabled;

    public ExtensionInfo(String name, String mainClass, String version, String author, String description) {
        this.name = name;
        this.mainClass = mainClass;
        this.version = version;
        this.author = author;
        this.description = description;
        this.loaded = false;
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public CloudExtension getCloudExtension() {
        return cloudExtension;
    }

    public ExtensionInfo setCloudExtension(CloudExtension cloudExtension) {
        this.cloudExtension = cloudExtension;
        return this;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public ExtensionInfo setLoaded(boolean loaded) {
        this.loaded = loaded;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ExtensionInfo setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
