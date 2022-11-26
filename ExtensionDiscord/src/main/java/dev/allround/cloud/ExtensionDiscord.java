package dev.allround.cloud;

import dev.allround.cloud.info.Listener;
import dev.allround.cloud.extention.CloudExtension;
import dev.allround.cloud.network.INetworkManager;

public class ExtensionDiscord implements CloudExtension {
    @Override
    public void init() {
        Cloud.getModule().getComponent(INetworkManager.class).registerClientPacketListener(new Listener());

        Cloud.getModule().getCloudLogger().info("Cloud Discord Extension initialised.");
    }

    @Override
    public void start() {
        Cloud.getModule().getCloudLogger().info("Cloud Discord Extension started.");
    }

    @Override
    public void stop() {
        Cloud.getModule().getCloudLogger().info("Cloud Discord Extension stopped.");
    }
}