package dev.allround.cloud.servicegroup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.service.IService;
import dev.allround.cloud.service.IServiceManager;
import dev.allround.cloud.util.Startable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface IServiceGroupManager extends Startable {
    IServiceGroup getServiceGroup(String name);

    List<IServiceGroup> getServiceGroups();

    /**
     * startet neue services von der angegebenen Gruppe falls notwendig. Oder setzt services auf die shutdown list
     */
    default void updateGroup(IServiceGroup iServiceGroup) {
        while (iServiceGroup.getOnlineServiceAmount() > iServiceGroup.getMaxOnlineAmount()) {
            iServiceGroup.getServices().sort(Comparator.comparing(o -> o.getPlayers().size()));
            IService iService = iServiceGroup.getServices().get(0);
            Cloud.getModule().getComponent(IServiceManager.class).queueStop(iService);
        }
        while (iServiceGroup.getOnlineServiceAmount() < iServiceGroup.getMaxOnlineAmount() && iServiceGroup.needNewService()){
            IService iService = Cloud.getModule().getComponent(IServiceManager.class).createService(iServiceGroup);
            Cloud.getModule().getComponent(IServiceManager.class).queueStart(iService);
        }

        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(iServiceGroup.createGroupInfoUpdatePacket());
    }

    default List<IServiceGroup> getServiceGroups(String node){
        return getServiceGroups().stream()
                .filter(iServiceGroup -> iServiceGroup.getNode().equals(node))
                .collect(Collectors.toList());
    }

    String getNodeWithMostLessGroups();

    default void registerServiceGroup(IServiceGroup serviceGroup) {
        if (getServiceGroups().stream().anyMatch(serviceGroup1 -> serviceGroup1.getGroupName().equalsIgnoreCase(serviceGroup.getGroupName())))
            return;

        getServiceGroups().add(serviceGroup);
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(serviceGroup.createGroupInfoUpdatePacket());
    }
}
