package dev.allround.cloud.network.listener.server;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleType;
import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.event.Event;
import dev.allround.cloud.event.IEventManager;
import dev.allround.cloud.network.*;

import java.util.ArrayList;
import java.util.UUID;

public class ServerPacketListener implements PacketListener {

    @PacketHandler(type = PacketType.REQUEST_NODE_INFO)
    public void onRequestNetworkAndSystemInfo(Packet packet) {
        ArrayList<String> responseInfo = new ArrayList<>();
        ModuleWrapper.getInstance().getModuleInfos().forEach(moduleInfo -> responseInfo.add(new Gson().toJson(moduleInfo)));
        INetworkServer networkServer = Cloud.getModule().getComponent(INetworkServer.class);
        CloudSocket requestSender = networkServer.getCloudSocketBySocketAddress(packet.getSenderSocket().get()).get();
        networkServer.sendPacket(new Packet(PacketType.RESPONSE_NODE_INFO, responseInfo.toArray(new String[0])).setRequestID(packet.getRequestID()), requestSender.netSocket());
    }



    @PacketHandler(type = PacketType.EVENT)
    public void onEvent(Packet packet) {
        Event event = new Gson().fromJson(packet.getData()[0], Event.class);
        Cloud.getModule().getComponent(IEventManager.class).handleEvent(event);
    }

    @PacketHandler(type = PacketType.API_KICK_PLAYER)
    public void onPlayerKick(Packet packet) {
        UUID playerUUID = UUID.fromString(packet.getData()[1]);
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.PROXY).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(new Packet(PacketType.API_KICK_PLAYER, packet.getData()[1]), cloudSocket.netSocket());
        });
    }


}
