package dev.allround.cloud.network.listener.client;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleInfo;
import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.network.*;

public class ClientPacketListener implements PacketListener {
    @PacketHandler(type = PacketType.NODE_CONNECT)
    public void onNodeConnect(Packet packet) {
        Cloud.getModule().getCloudLogger().info("New Node connected: " + packet.getData()[0]);
        ModuleWrapper.getInstance().registerModule(new Gson().fromJson(packet.getData()[0], ModuleInfo.class));
    }

    @PacketHandler(type = PacketType.NODE_DISCONNECT)
    public void onNodeDisconnect(Packet packet) {
        Cloud.getModule().getCloudLogger().info("New Node disconnected: " + packet.getData()[0]);
        ModuleWrapper.getInstance().unregisterModule(new Gson().fromJson(packet.getData()[0], ModuleInfo.class));
    }

    @PacketHandler(type = PacketType.SERVER_STATUS_KEEP_ALIVE)
    public void onKeepAlive(Packet packet) {
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.CLIENT_STATUS_KEEP_ALIVE).setRequestID(packet.getRequestID()));
    }

}
