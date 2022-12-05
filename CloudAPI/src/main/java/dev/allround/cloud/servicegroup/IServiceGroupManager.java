package dev.allround.cloud.servicegroup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleInfo;
import dev.allround.cloud.ModuleType;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface IServiceGroupManager extends Startable, Stopable {
    Optional<IServiceGroup> getServiceGroup(String name);

    List<IServiceGroup> getServiceGroups();

    void loadGroups();
    void saveGroups();
    void updateGroups(IServiceGroup... iServiceGroups);

    default List<IServiceGroup> getServiceGroups(String node){
        return getServiceGroups().stream()
                .filter(iServiceGroup -> iServiceGroup.getNode().equals(node))
                .collect(Collectors.toList());
    }

    void update(IServiceGroup iServiceGroup);

    default ModuleInfo getNodeWithLowestGroupCount(){
        return Cloud.getWrapper().getModuleInfos().stream()
                .filter(moduleInfo -> moduleInfo.getType() == ModuleType.NODE)
                .sorted(Comparator.comparingInt(node -> getServiceGroups().stream().filter(iServiceGroup -> iServiceGroup.getNode().equals(node.name())).toList().size()))
                .toList()
                .get(0)
                ;
    }

    default void registerServiceGroup(IServiceGroup serviceGroup) {
        if (getServiceGroups().stream().anyMatch(serviceGroup1 -> serviceGroup1.getGroupName().equalsIgnoreCase(serviceGroup.getGroupName())))
            return;

        getServiceGroups().add(serviceGroup);
        if (Cloud.getWrapper().isThisModule(serviceGroup.getNode())){
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(serviceGroup.createGroupInfoUpdatePacket());
        }
    }
}
