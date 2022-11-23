package dev.allround.cloud.network.intern;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleType;
import dev.allround.cloud.network.*;
import io.vertx.core.net.SocketAddress;

public class SocketAuthPacketListener implements PacketListener {
    @PacketHandler(type = PacketType.SOCKET_AUTH)
    public void onSocketAuth(Packet packet) {
        ModuleType moduleType = ModuleType.valueOf(packet.getData()[0]);
        SocketAddress socketAddress = SocketAddress.inetSocketAddress(Integer.parseInt(packet.getData()[1]), packet.getData()[2]);
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSocketBySocketAddress(socketAddress).get().setType(moduleType);
        Cloud.getModule().getComponent(INetworkServer.class).broadcastPacket(new Packet(PacketType.SOCKET_AUTH_SUCCESS).setRequestID(packet.getRequestID()));
        Cloud.getModule().getCloudLogger().debug("[Network - Server] Socket auth done: " + socketAddress + " - " + moduleType.name());
    }


}
