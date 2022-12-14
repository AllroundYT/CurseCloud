package dev.allround.cloud.extention;

public interface IExtensionManager {

    void loadExtensions();

    void unloadExtensions();

    void loadExtension(ExtensionInfo extensionInfo);

    void startExtension(ExtensionInfo extensionInfo);

    void stopExtension(ExtensionInfo extensionInfo);

    void loadExtensionInfos();
}
