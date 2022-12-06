package dev.allround.cloud.network.listener.server;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleType;
import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.network.*;
import dev.allround.cloud.player.IPlayerManager;
import dev.allround.cloud.service.IService;
import dev.allround.cloud.service.IServiceManager;
import dev.allround.cloud.service.Service;
import dev.allround.cloud.servicegroup.IServiceGroup;
import dev.allround.cloud.servicegroup.IServiceGroupManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerPacketListener implements PacketListener {

    @PacketHandler(type = PacketType.API_STOP_CLOUD)
    public void onAPIStopCloud(Packet packet) {
        INetworkServer networkServer = Cloud.getModule().getComponent(INetworkServer.class);
        CloudSocket requestSender = networkServer.getCloudSocketBySocketAddress(packet.getSenderSocket().get()).get();
        networkServer.sendPacket(new Packet(PacketType.UNDEFINED_API_RESPONSE, "SUCCESS").setRequestID(packet.getRequestID()), requestSender.netSocket());
        System.exit(0);
    }

    @PacketHandler(type = PacketType.REQUEST_NODE_INFO)
    public void onRequestNodeInfo(Packet packet) {
        ArrayList<String> responseInfo = new ArrayList<>();
        ModuleWrapper.getInstance().getModuleInfos().forEach(moduleInfo -> responseInfo.add(new Gson().toJson(moduleInfo)));
        INetworkServer networkServer = Cloud.getModule().getComponent(INetworkServer.class);
        CloudSocket requestSender = networkServer.getCloudSocketBySocketAddress(packet.getSenderSocket().get()).get();
        networkServer.sendPacket(new Packet(PacketType.RESPONSE_NODE_INFO, responseInfo.toArray(new String[0])).setRequestID(packet.getRequestID()), requestSender.netSocket());
    }

    @PacketHandler(type = PacketType.CUSTOM_REQUEST)
    public void onCustomRequest(Packet packet){
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.EXTENSION).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(packet, cloudSocket.netSocket());
        });
    }

    /**
     * To read the data:
     * <code>
     * String[] data = packet.getData();
     * List<String[]> serviceInfos = new ArrayList<>();  -> List with data from a SERVICE_INFO_UPDATE Packet
     * for (String d : data){
     * serviceInfos.add(new Gson().fromJson(d,String[].class))
     * }
     * </code>
     */

    @PacketHandler(type = PacketType.REQUEST_SERVICE_INFO)
    public void onRequestServiceInfo(Packet packet) {
        INetworkServer networkServer = Cloud.getModule().getComponent(INetworkServer.class);
        List<String> serviceData = new ArrayList<>();
        Cloud.getModule().getComponent(IServiceManager.class).getServices().forEach(service -> {
            serviceData.add(service.createServiceInfoUpdatePacket().getData()[0]);
        });
        CloudSocket requestSender = networkServer.getCloudSocketBySocketAddress(packet.getSenderSocket().get()).get();
        networkServer.sendPacket(new Packet(PacketType.RESPONSE_SERVICE_INFO, serviceData.toArray(new String[0])).setRequestID(packet.getRequestID()), requestSender.netSocket());
    }

    /**
     * To read the data:
     * <code>
     * String[] data = packet.getData();
     * List<String[]> groupInfos = new ArrayList<>();  -> List with data from a GROUP_INFO_UPDATE Packet
     * for (String d : data){
     * groupInfos.add(new Gson().fromJson(d,String[].class))
     * }
     * </code>
     */
    @PacketHandler(type = PacketType.REQUEST_GROUP_INFO)
    public void onRequestGroupInfo(Packet packet) {
        INetworkServer networkServer = Cloud.getModule().getComponent(INetworkServer.class);
        CloudSocket requestSender = networkServer.getCloudSocketBySocketAddress(packet.getSenderSocket().get()).get();
        List<String> groupData = new ArrayList<>();
        Cloud.getModule().getComponent(IServiceGroupManager.class).getServiceGroups().forEach(group -> {
            groupData.add(group.createGroupInfoUpdatePacket().getData()[0]);
        });
        networkServer.sendPacket(new Packet(PacketType.RESPONSE_GROUP_INFO, groupData.toArray(new String[0])).setRequestID(packet.getRequestID()), requestSender.netSocket());
    }

    /**
     * To read the data:
     * <code>
     * String[] data = packet.getData();
     * List<String[]> playerInfos = new ArrayList<>();  -> List with data from a PLAYER_INFO_UPDATE Packet
     * for (String d : data){
     * playerInfos.add(new Gson().fromJson(d,String[].class))
     * }
     * </code>
     */
    @PacketHandler(type = PacketType.REQUEST_PLAYER_INFO)
    public void onRequestPlayerInfo(Packet packet) {
        INetworkServer networkServer = Cloud.getModule().getComponent(INetworkServer.class);
        CloudSocket requestSender = networkServer.getCloudSocketBySocketAddress(packet.getSenderSocket().get()).get();
        List<String> playerData = new ArrayList<>();
        Cloud.getModule().getComponent(IPlayerManager.class).getCloudPlayers().forEach(iCloudPlayer -> {
            playerData.add(iCloudPlayer.createPlayerInfoUpdatePacket().getData()[0]);
        });
        networkServer.sendPacket(new Packet(PacketType.RESPONSE_PLAYER_INFO, playerData.toArray(new String[0])).setRequestID(packet.getRequestID()), requestSender.netSocket());
    }

    @PacketHandler(type = PacketType.API_KICK_PLAYER)
    public void onPlayerKick(Packet packet) {
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.PROXY).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(packet, cloudSocket.netSocket());
        });
    }

    @PacketHandler(type = PacketType.API_SEND_MSG_TO_PLAYER)
    public void onSendMessageToPlayer(Packet packet) {
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.PROXY).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(packet, cloudSocket.netSocket());
        });
    }

    @PacketHandler(type = PacketType.API_SEND_PLAYER)
    public void onSendPlayer(Packet packet) {
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.PROXY).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(packet, cloudSocket.netSocket());
        });
    }

    @PacketHandler(type = PacketType.API_START_SERVICE)
    public void onStartService(Packet packet) {
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.NODE).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(packet, cloudSocket.netSocket());
        });
    }

    @PacketHandler(type = PacketType.API_START_SERVICE_OF_GROUP)
    public void onStartServiceOfGroup(Packet packet) {
        INetworkServer networkServer = Cloud.getModule().getComponent(INetworkServer.class);
        CloudSocket requestSender = networkServer.getCloudSocketBySocketAddress(packet.getSenderSocket().get()).get();
        String serviceGroupName = packet.getData()[0];
        Optional<IServiceGroup> optionalServiceGroup = Cloud.getModule().getComponent(IServiceGroupManager.class).getServiceGroup(serviceGroupName);
        if (optionalServiceGroup.isEmpty()) {
            networkServer.sendPacket(new Packet(PacketType.API_RESULT_START_SERVICE, "ERROR", "No ServiceGroup found with this name.").setRequestID(packet.getRequestID()), requestSender.netSocket());
        } else {
            IServiceGroup iServiceGroup = optionalServiceGroup.get();
            IService iService = new Service(iServiceGroup);
            iService.start();
            networkServer.sendPacket(new Packet(PacketType.API_RESULT_START_SERVICE, "SUCCESS", "Service created.").setRequestID(packet.getRequestID()), requestSender.netSocket());
        }
    }

    @PacketHandler(type = PacketType.API_STOP_SERVICE)
    public void onStopService(Packet packet) {
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.NODE).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(packet, cloudSocket.netSocket());
        });
    }

    @PacketHandler(type = PacketType.API_UPDATE_SERVICE_GROUP)
    public void onUpdateServiceGroup(Packet packet) {
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.NODE).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(packet, cloudSocket.netSocket());
        });
    }

    @PacketHandler(type = PacketType.API_STOP_SERVICE_GROUP)
    public void onStopServiceGroup(Packet packet) {
        Cloud.getModule().getComponent(INetworkServer.class).getCloudSockets(ModuleType.NODE).forEach(cloudSocket -> {
            Cloud.getModule().getComponent(INetworkServer.class).sendPacket(packet, cloudSocket.netSocket());
        });
    }
}
