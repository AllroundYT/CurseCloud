package dev.allround.cloud.network.listener;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.*;
import dev.allround.cloud.player.ICloudPlayer;
import dev.allround.cloud.player.IPlayerManager;
import dev.allround.cloud.service.IServiceManager;

import java.util.Optional;
import java.util.UUID;

public class NetworkListener implements PacketListener {
    @PacketHandler(type = PacketType.SERVER_STATUS_KEEP_ALIVE)
    public void onKeepAlive(Packet packet) {
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.CLIENT_STATUS_KEEP_ALIVE).setRequestID(packet.getRequestID()));
    }

    @PacketHandler(type = PacketType.API_KICK_PLAYER)
    public void apiPlayerKick(Packet packet) {
        UUID uuid = UUID.fromString(packet.getData()[0]);
        Optional<ICloudPlayer> optionalPlayer = Cloud.getModule().getComponent(IPlayerManager.class).getCloudPlayer(uuid);

        if (optionalPlayer.isEmpty()) return;

        ICloudPlayer player = optionalPlayer.get();

        if (!player.getProxy().equals(Cloud.getWrapper().getThisModule().name())) return;
        player.kick(packet.getData()[1]);
    }

    @PacketHandler(type = PacketType.API_SEND_PLAYER)
    public void apiSendPlayer(Packet packet){
        UUID uuid = UUID.fromString(packet.getData()[0]);
        String service = packet.getData()[1];
        Optional<ICloudPlayer> optionalPlayer = Cloud.getModule().getComponent(IPlayerManager.class).getCloudPlayer(uuid);

        if (optionalPlayer.isEmpty()) return;

        ICloudPlayer player = optionalPlayer.get();

        if (!player.getProxy().equals(Cloud.getWrapper().getThisModule().name())) return;
        if (Cloud.getModule().getComponent(IServiceManager.class).getService(service).isEmpty()) return;
        player.send(Cloud.getModule().getComponent(IServiceManager.class).getService(service).get());
    }

    @PacketHandler(type = PacketType.API_SEND_MSG_TO_PLAYER)
    public void apiSendMsgToPlayer(Packet packet){
        UUID uuid = UUID.fromString(packet.getData()[0]);
        String msg = packet.getData()[1];
        Optional<ICloudPlayer> optionalPlayer = Cloud.getModule().getComponent(IPlayerManager.class).getCloudPlayer(uuid);

        if (optionalPlayer.isEmpty()) return;

        ICloudPlayer player = optionalPlayer.get();

        if (!player.getProxy().equals(Cloud.getWrapper().getThisModule().name())) return;
        player.sendMessage(msg);
    }
}
