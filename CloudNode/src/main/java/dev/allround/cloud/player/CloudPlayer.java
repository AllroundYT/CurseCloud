package dev.allround.cloud.player;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.IService;

import java.util.List;
import java.util.UUID;

public class CloudPlayer implements ICloudPlayer {
    private final UUID uuid;
    private final String name;
    private boolean online;
    private String service;
    private String proxy;
    private boolean operator;

    public CloudPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
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
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_SEND_MSG_TO_PLAYER,getUuid().toString(),String.valueOf(msg)));
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
        return isOnline();
    }


    public CloudPlayer setProxy(String proxy) {
        this.proxy = proxy;
        return this;
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public String getProxy() {
        return proxy;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public void kick(String reason) {
        if (!isOnline()) return;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_KICK_PLAYER, this.uuid.toString()), packet -> {
            Cloud.getModule().getCloudLogger().info("[Player Management] Player kicked -> " + getName());
        });
    }

    @Override
    public void send(IService iService) {
        if (!isOnline()) return;
        String cachedService = getService();
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_SEND_PLAYER, this.uuid.toString(), iService.getServiceID()), packet -> {
            Cloud.getModule().getCloudLogger().info("[Player Management] Sent " + cachedService + " from " + getService() + " to " + iService.getServiceID());
            setService(iService.getServiceID());
        });
    }

    @Override
    public List<String> getData() {
        return null;
    }
}
