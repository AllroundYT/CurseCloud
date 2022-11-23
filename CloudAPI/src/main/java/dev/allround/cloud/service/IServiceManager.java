package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.servicegroup.IServiceGroup;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;

import java.util.List;
import java.util.Optional;

public interface IServiceManager extends Startable, Stopable {
    List<IService> getServices();
    List<IService> createServices(IServiceGroup iServiceGroup, int amount);
    IService createService(IServiceGroup iServiceGroup);
    Optional<IService> getService(String serviceId);
    List<IService> getStartList();

    default void queueStart(IService iService){
        if (getServices().stream().anyMatch(iService1 -> iService1.getServiceID().equals(iService.getServiceID()))) return;
        getStartList().add(iService);
        Cloud.getModule().getCloudLogger().info("Service added to start queue: "+iService.getServiceID());
    }
    default void queueStop(IService iService){
        getStopList().add(iService);
        Cloud.getModule().getCloudLogger().info("Service added to stop queue: "+iService.getServiceID()+" Player Count: "+iService.getPlayers().size());
    }
    List<IService> getStopList();
}
