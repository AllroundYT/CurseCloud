package dev.allround.cloud.network;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleInfo;
import dev.allround.cloud.ModuleType;
import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.player.CloudPlayer;
import dev.allround.cloud.player.CloudPlayerInfoSnapshot;
import dev.allround.cloud.player.IPlayerManager;
import dev.allround.cloud.service.*;
import dev.allround.cloud.servicegroup.IServiceGroupManager;
import dev.allround.cloud.servicegroup.ServiceGroup;
import dev.allround.cloud.servicegroup.ServiceGroupInfoSnapshot;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;

import java.util.Arrays;

public class NetworkClient extends INetworkClient {
    public NetworkClient(INetworkManager manager) {
        super(manager);
    }

    @Override
    public void onConnectionSuccess(NetSocket netSocket) {
        sendPacket(new Packet(PacketType.SOCKET_AUTH, ModuleType.NODE.name(), String.valueOf(netSocket.localAddress().port()), netSocket.localAddress().host()), packet -> {
            sendPacket(new Packet(PacketType.SERVICE_CONNECT, new Gson().toJson(Cloud.getModule().getComponent(ModuleInfo.class))));
        });
        sendPacket(PacketType.REQUEST_NODE_INFO, new String[0], response -> {
            for (String data : response.getData()) {
                ModuleInfo moduleInfo = new Gson().fromJson(data, ModuleInfo.class);
                ModuleWrapper.getInstance().registerModule(moduleInfo);
            }
        });
        sendPacket(PacketType.REQUEST_PLAYER_INFO, new String[0], response -> {
            for (String data : response.getData()) {
                CloudPlayerInfoSnapshot snapshot = new Gson().fromJson(data, CloudPlayerInfoSnapshot.class);
                Cloud.getModule().getComponent(IPlayerManager.class).getCloudPlayers().add(new CloudPlayer(snapshot.getUuid(), snapshot.getName(), snapshot.isOnline(), snapshot.getService(), snapshot.getProxy(), snapshot.isOperator(), Arrays.asList(snapshot.getData())));
            }
        });
        sendPacket(PacketType.REQUEST_GROUP_INFO, new String[0], response -> {
            for (String data : response.getData()) {
                ServiceGroupInfoSnapshot snapshot = new Gson().fromJson(data, ServiceGroupInfoSnapshot.class);
                Cloud.getModule().getComponent(IServiceGroupManager.class).getServiceGroups().add(new ServiceGroup(ServiceType.valueOf(snapshot.getServiceType()), snapshot.getNode(), snapshot.getMinOnlineAmount(), snapshot.getMaxOnlineAmount(), snapshot.getMaxPlayers(), snapshot.getGroupName(), snapshot.getMaxRam(), snapshot.getPercentageToStartNewService(), ServiceVersion.valueOf(snapshot.getServiceVersion()), snapshot.getJavaParams(), snapshot.getStartArgs()));
            }
        });
        sendPacket(PacketType.REQUEST_SERVICE_INFO, new String[0], response -> {
            for (String data : response.getData()) {
                ServiceInfoSnapshot snapshot = new Gson().fromJson(data, ServiceInfoSnapshot.class);
                Cloud.getModule().getComponent(IServiceManager.class).getServices().add(new Service(snapshot.getNode(), SocketAddress.inetSocketAddress(snapshot.getPort(), snapshot.getHost()), ServiceType.valueOf(snapshot.getServiceType()), ServiceVersion.valueOf(snapshot.getServiceVersion()), snapshot.getGroupName(), snapshot.getServiceID(), snapshot.getJavaParams(), snapshot.getMaxRam(), snapshot.getStartArgs(), new String[]{snapshot.getMotd1(), snapshot.getMotd2()}, snapshot.getStatus(), snapshot.getMaxPlayers(), null));
            }
        });
    }
}
