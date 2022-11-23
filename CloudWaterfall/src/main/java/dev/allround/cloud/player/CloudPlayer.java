package dev.allround.cloud.player;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.IService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;

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
    @Override
    public void sendMessage(Object msg) {
        if (!Cloud.getWrapper().getThisModule().name().equals(getProxy())) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(
                    new Packet(
                            PacketType.API_SEND_MSG_TO_PLAYER,
                            getUuid().toString(),
                            String.valueOf(msg)
                    )
            );
            return;
        }
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(getUuid());
        player.sendMessage(String.valueOf(msg));
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
        if (!Cloud.getWrapper().getThisModule().name().equals(getProxy())) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(
                    new Packet(
                            PacketType.API_KICK_PLAYER,
                            getUuid().toString(),
                            reason
                    )
            );
            return;
        }
        ProxyServer.getInstance().getPlayer(getUuid()).disconnect(reason);
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(
                PacketType.PLAYER_KICKED,
                getUuid().toString(),
                reason
        ));
    }



    @Override
    public List<String> getData() {
        return null;
    }

    @Override
    public void send(IService iService) {
        if (!Cloud.getWrapper().getThisModule().name().equals(getProxy())) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(
                    new Packet(
                            PacketType.API_SEND_PLAYER,
                            getUuid().toString(),
                            iService.getServiceID()
                    )
            );
            return;
        }
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(getUuid());
        proxiedPlayer.connect(ProxyServer.getInstance().getServerInfo(iService.getServiceID()), ServerConnectEvent.Reason.PLUGIN);
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(
                PacketType.PLAYER_SWITCH_SERVICE,
                getUuid().toString(),
                getService(),
                iService.getServiceID()
        ));
        this.service = iService.getServiceID();
    }
}
