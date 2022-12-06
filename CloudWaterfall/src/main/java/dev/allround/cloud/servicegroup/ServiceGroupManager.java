package dev.allround.cloud.servicegroup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.service.ServiceVersion;
import dev.allround.cloud.util.Document;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Getter
public class ServiceGroupManager implements IServiceGroupManager{
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
        if (getServiceGroup(iServiceGroup.getGroupName()).isEmpty()){
            getServiceGroups().add(iServiceGroup);
        }else {
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
