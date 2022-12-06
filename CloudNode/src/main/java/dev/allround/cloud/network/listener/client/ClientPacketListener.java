package dev.allround.cloud.network.listener.client;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleInfo;
import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.network.*;
import dev.allround.cloud.service.*;
import dev.allround.cloud.servicegroup.IServiceGroup;
import dev.allround.cloud.servicegroup.IServiceGroupManager;
import dev.allround.cloud.servicegroup.ServiceGroup;
import dev.allround.cloud.servicegroup.ServiceGroupInfoSnapshot;
import io.vertx.core.net.SocketAddress;

import java.util.Optional;

public class ClientPacketListener implements PacketListener {
    @PacketHandler(type = PacketType.NODE_CONNECT)
    public void onNodeConnect(Packet packet) {
        Cloud.getModule().getCloudLogger().info("New Node connected: " + packet.getData()[0]);
        ModuleWrapper.getInstance().registerModule(new Gson().fromJson(packet.getData()[0], ModuleInfo.class));
    }

    @PacketHandler(type = PacketType.NODE_DISCONNECT)
    public void onNodeDisconnect(Packet packet) {
        Cloud.getModule().getCloudLogger().info("Node disconnected: " + packet.getData()[0]);
        ModuleWrapper.getInstance().unregisterModule(new Gson().fromJson(packet.getData()[0], ModuleInfo.class));
    }

    @PacketHandler(type = PacketType.SERVICE_DISCONNECT)
    public void onServiceDisconnect(Packet packet) {
        Cloud.getModule().getCloudLogger().info("Service disconnected: " + new Gson().fromJson(packet.getData()[0], ModuleInfo.class).name());
        ModuleInfo moduleInfo = new Gson().fromJson(packet.getData()[0], ModuleInfo.class);
        ModuleWrapper.getInstance().unregisterModule(moduleInfo);

        String serviceId = moduleInfo.name();
        if (Cloud.getModule().getComponent(IServiceManager.class).getService(serviceId).isPresent()) {
            IService service = Cloud.getModule().getComponent(IServiceManager.class).getService(serviceId).get();
            service.setStatus("OFFLINE");
        }
    }

    @PacketHandler(type = PacketType.SERVICE_CONNECT)
    public void onServiceConnect(Packet packet) {
        Cloud.getModule().getCloudLogger().info("Service connected: " + new Gson().fromJson(packet.getData()[0], ModuleInfo.class).name());
        ModuleInfo moduleInfo = new Gson().fromJson(packet.getData()[0], ModuleInfo.class);
        ModuleWrapper.getInstance().registerModule(moduleInfo);

        String serviceId = moduleInfo.name();
        if (Cloud.getModule().getComponent(IServiceManager.class).getService(serviceId).isPresent()) {
            IService service = Cloud.getModule().getComponent(IServiceManager.class).getService(serviceId).get();
            service.setStatus("CONNECTED");
        }
    }

    @PacketHandler(type = PacketType.CLUSTER_STOP)
    public void onClusterStop(Packet packet) {
        System.exit(0);
    }

    @PacketHandler(type = PacketType.SERVER_STATUS_KEEP_ALIVE)
    public void onKeepAlive(Packet packet) {
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(new Packet(PacketType.CLIENT_STATUS_KEEP_ALIVE).setRequestID(packet.getRequestID()));
    }

    @PacketHandler(type = PacketType.SERVICE_INFO_UPDATE)
    public void onServiceInfoUpdate(Packet packet) {
        ServiceInfoSnapshot snapshot = new Gson().fromJson(packet.getData()[0], ServiceInfoSnapshot.class);
        IService service = new Service(snapshot.getNode(), SocketAddress.inetSocketAddress(snapshot.getPort(), snapshot.getHost()), ServiceType.valueOf(snapshot.getServiceType()), ServiceVersion.valueOf(snapshot.getServiceVersion()), snapshot.getGroupName(), snapshot.getServiceID(), snapshot.getJavaParams(), snapshot.getMaxRam(), snapshot.getStartArgs(), new String[]{snapshot.getMotd1(), snapshot.getMotd2()}, snapshot.getStatus(), snapshot.getMaxPlayers(), null);
        Optional<IService> oldService = Cloud.getModule().getComponent(IServiceManager.class).getService(snapshot.getServiceID());
        Cloud.getModule().getComponent(IServiceManager.class).update(service);

    }

    @PacketHandler(type = PacketType.GROUP_INFO_UPDATE)
    public void onGroupInfoUpdate(Packet packet) {
        ServiceGroupInfoSnapshot snapshot = new Gson().fromJson(packet.getData()[0], ServiceGroupInfoSnapshot.class);
        IServiceGroup serviceGroup = new ServiceGroup(ServiceType.valueOf(snapshot.getServiceType()), snapshot.getNode(), snapshot.getGroupName(), ServiceVersion.valueOf(snapshot.getServiceVersion()), snapshot.getJavaParams(), snapshot.getStartArgs(), snapshot.getMinOnlineAmount(), snapshot.getMaxOnlineAmount(), snapshot.getMaxPlayers(), snapshot.getMaxRam(), snapshot.getPercentageToStartNewService());
        Optional<IServiceGroup> optionalOldGroup = Cloud.getModule().getComponent(IServiceGroupManager.class).getServiceGroup(snapshot.getGroupName());
        if (optionalOldGroup.isEmpty()) {
            Cloud.getModule().getCloudLogger().info("Service Group has been created -> " + serviceGroup.getGroupName());
        } else {
            IServiceGroup oldGroup = optionalOldGroup.get();
            Cloud.getModule().getCloudLogger().info("Service Group has been updated -> " + serviceGroup.getGroupName());
            if (oldGroup.getMinOnlineAmount() != serviceGroup.getMinOnlineAmount())
                Cloud.getModule().getCloudLogger().info(" - Min Online Amount changed: " + oldGroup.getMinOnlineAmount() + " -> " + serviceGroup.getMinOnlineAmount());
            if (oldGroup.getMaxOnlineAmount() != serviceGroup.getMaxOnlineAmount())
                Cloud.getModule().getCloudLogger().info(" - Max Online Amount changed: " + oldGroup.getMaxOnlineAmount() + " -> " + serviceGroup.getMaxOnlineAmount());
            if (oldGroup.getMaxRam() != serviceGroup.getMaxRam())
                Cloud.getModule().getCloudLogger().info(" - Max Online Amount changed: " + oldGroup.getMaxRam() + " -> " + serviceGroup.getMaxRam());
            if (oldGroup.getPercentageToStartNewService() != serviceGroup.getPercentageToStartNewService())
                Cloud.getModule().getCloudLogger().info(" - Max Online Amount changed: " + oldGroup.getPercentageToStartNewService() + " -> " + serviceGroup.getPercentageToStartNewService());
        }
        Cloud.getModule().getComponent(IServiceGroupManager.class).update(serviceGroup);
    }
}
