package dev.allround.cloud.network;

import dev.allround.cloud.Cloud;
import io.vertx.core.net.NetSocket;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class NetworkServer extends INetworkServer {
    public NetworkServer(INetworkManager manager) {
        super(manager);
        HashMap<NetSocket, Long> lastKeepAlive = new HashMap<>();
        Cloud.getModule().getScheduledExecutorService().scheduleAtFixedRate(() -> {
            //Keep Alive cycle
            getConnectedSockets().forEach(netSocket -> {
                lastKeepAlive.putIfAbsent(netSocket.netSocket(), System.currentTimeMillis());
                if (System.currentTimeMillis() - lastKeepAlive.get(netSocket) > 21000) {
                    netSocket.netSocket().close();
                    Cloud.getModule().getCloudLogger().warn("[Network - Server] NetSocket timeout! " + netSocket.netSocket().remoteAddress());
                    return;
                }
                sendPacket(new Packet(PacketType.SERVER_STATUS_KEEP_ALIVE), netSocket.netSocket(), packet -> lastKeepAlive.put(netSocket.netSocket(), System.currentTimeMillis()));
            });
        }, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onClientConnect(NetSocket netSocket) {
    }

}
