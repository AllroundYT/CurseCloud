package dev.allround.cloud.command;

import java.util.UUID;

public interface ICommandSender {

    void sendMessage(Object msg);

    boolean isConsole();

    String getName();

    UUID getUuid();

    boolean hasPermission(String perm);
}
