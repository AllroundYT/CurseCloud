package dev.allround.cloud.network;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleType;
import io.vertx.core.net.NetSocket;

public class NetworkClient extends INetworkClient {
    public NetworkClient(INetworkManager manager) {
        super(manager);
    }

    @Override
    public void onConnectionSuccess(NetSocket netSocket) {
        sendPacket(new Packet(PacketType.SOCKET_AUTH, ModuleType.NODE.name(), String.valueOf(netSocket.localAddress().port()), netSocket.localAddress().host()), packet -> Cloud.getModule().getCloudLogger().debug("[Network - Client] Socket auth done!"));
    }
}
