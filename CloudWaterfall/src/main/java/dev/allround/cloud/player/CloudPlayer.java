package dev.allround.cloud.player;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.IService;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;

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

    @Override
    public void sendMessage(Object msg) {
        if (Cloud.getWrapper().isNotThisModule(getProxy())) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_SEND_MSG_TO_PLAYER, getUuid().toString(), String.valueOf(msg)));
            return;
        }
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(getUuid());
        player.sendMessage("§7[§bCloud§7] "+ String.valueOf(msg));
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean hasPermission(String perm) {
        return isOperator();
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public String getProxy() {
        return proxy;
    }

    public boolean isOperator() {
        return operator;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public void kick(String reason) {
        if (Cloud.getWrapper().isNotThisModule(getProxy())) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_KICK_PLAYER, getUuid().toString(), reason));
            return;
        }
        ProxyServer.getInstance().getPlayer(getUuid()).disconnect(reason);
        this.service = null;
        this.proxy = null;
        this.online = false;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createPlayerInfoUpdatePacket());
    }

    @Override
    public void clonePlayerInfo(ICloudPlayer cloudPlayer) {
        this.online = cloudPlayer.isOnline();
        this.operator = cloudPlayer.isOperator();
        this.service = cloudPlayer.getService();
        this.proxy = cloudPlayer.getProxy();
        this.data.clear();
        this.data.addAll(cloudPlayer.getData());
    }

    @Override
    public List<String> getData() {
        return data;
    }

    @Override
    public void send(IService iService) {
        if (Cloud.getWrapper().isNotThisModule(getProxy())) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.API_SEND_PLAYER, getUuid().toString(), iService.getServiceID()));
            return;
        }
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(getUuid());
        proxiedPlayer.connect(ProxyServer.getInstance().getServerInfo(iService.getServiceID()), ServerConnectEvent.Reason.PLUGIN);
        this.service = iService.getServiceID();
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createPlayerInfoUpdatePacket());
    }
}
