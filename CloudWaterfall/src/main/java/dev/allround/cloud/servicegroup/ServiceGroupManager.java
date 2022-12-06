package dev.allround.cloud.servicegroup;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class ServiceGroupManager implements IServiceGroupManager {
    private final List<IServiceGroup> serviceGroups;

    public ServiceGroupManager() {
        this.serviceGroups = new ArrayList<>();
    }

    @Override
    public Optional<IServiceGroup> getServiceGroup(String name) {
        return getServiceGroups().stream().filter(iServiceGroup -> iServiceGroup.getGroupName().equals(name)).findFirst();
    }

    @Override
    public void loadGroups() {
    }

    @Override
    public void update(IServiceGroup iServiceGroup) {
        if (getServiceGroup(iServiceGroup.getGroupName()).isEmpty()) {
            getServiceGroups().add(iServiceGroup);
        } else {
            getServiceGroup(iServiceGroup.getGroupName()).get().cloneGroupInfo(iServiceGroup);
        }
    }

    @Override
    public void saveGroups() {
    }

    @Override
    public void updateGroups(IServiceGroup... iServiceGroups) {
        for (IServiceGroup iServiceGroup : iServiceGroups) {
            iServiceGroup.update();
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
