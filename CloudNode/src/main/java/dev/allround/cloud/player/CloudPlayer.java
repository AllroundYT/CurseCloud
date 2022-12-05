package dev.allround.cloud.player;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.IService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CloudPlayer implements ICloudPlayer {
    private final UUID uuid;
    private final String name;
    private boolean online;
    private String service;
    private String proxy;
    private boolean operator;
    private final List<String> data;

    public CloudPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.data = new ArrayList<>();
    }

    public boolean isOperator() {
        return operator;
    }

    public CloudPlayer setOperator(boolean operator) {
        this.operator = operator;
        return this;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    public CloudPlayer setOnline(boolean online) {
        this.online = online;
        return this;
    }

    @Override
    public void sendMessage(Object msg) {
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_SEND_MSG_TO_PLAYER, getUuid().toString(), String.valueOf(msg)));
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public boolean hasPermission(String perm) {
        return isOperator();
    }

    @Override
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String getProxy() {
        return proxy;
    }

    public CloudPlayer setProxy(String proxy) {
        this.proxy = proxy;
        return this;
    }

    @Override
    public void kick(String reason) {
        if (!isOnline()) return;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_KICK_PLAYER, this.uuid.toString(), reason));
    }

    @Override
    public void send(IService iService) {
        if (!isOnline()) return;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_SEND_PLAYER, this.uuid.toString(), iService.getServiceID()));
    }

    @Override
    public List<String> getData() {
        return data;
    }
}
