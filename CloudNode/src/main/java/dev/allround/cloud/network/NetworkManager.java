package dev.allround.cloud.network;

import dev.allround.cloud.network.listener.client.ClientPacketListener;
import dev.allround.cloud.network.listener.server.ServerPacketListener;

public class NetworkManager extends INetworkManager {
    @Override
    public void init() {
        registerClientPacketListener(new ClientPacketListener());
        registerServerPacketListener(new ServerPacketListener());
    }
}
