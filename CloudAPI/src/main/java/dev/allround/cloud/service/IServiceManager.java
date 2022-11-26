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
    void queueStart(IService iService);
    void queueStop(IService iService);
    List<IService> getStopList();
}
