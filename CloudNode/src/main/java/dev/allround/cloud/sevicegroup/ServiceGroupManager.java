package dev.allround.cloud.sevicegroup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleInfo;
import dev.allround.cloud.ModuleType;
import dev.allround.cloud.servicegroup.IServiceGroup;
import dev.allround.cloud.servicegroup.IServiceGroupManager;

import java.util.List;

public class ServiceGroupManager implements IServiceGroupManager {
    @Override
    public IServiceGroup getServiceGroup(String name) {
        return null;
    }

    @Override
    public List<IServiceGroup> getServiceGroups() {
        return null;
    }

    @Override
    public String getNodeWithMostLessGroups() {
        String node = Cloud.getWrapper().getThisModule().name();
        for (ModuleInfo moduleInfo : Cloud.getWrapper().getModuleInfos()){
            if (moduleInfo.getType().equals(ModuleType.NODE)){
                if (getServiceGroups(moduleInfo.name()).size() < getServiceGroups(node).size()){
                    node = moduleInfo.name();
                }
            }
        }
        return node;
    }

    @Override
    public void start() {

    }
}
