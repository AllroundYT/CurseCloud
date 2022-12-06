package dev.allround.cloud.command;

import dev.allround.cloud.Cloud;

import java.util.UUID;

public class ConsoleSender implements ICommandSender {
    private final UUID uuid = UUID.randomUUID();

    @Override
    public void sendMessage(Object msg) {
        Cloud.getModule().getCloudLogger().info(msg.toString().replaceAll("§.", ""));
    }

    @Override
    public boolean isConsole() {
        return true;
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean hasPermission(String perm) {
        return isConsole();
    }
}
