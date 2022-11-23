package dev.allround.cloud.network;

import dev.allround.cloud.network.listener.NetworkListener;

public class NetworkManager extends INetworkManager {
    @Override
    public void init() {
        registerClientPacketListener(new NetworkListener());
    }
}
