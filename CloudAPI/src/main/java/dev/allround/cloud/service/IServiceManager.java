package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.servicegroup.IServiceGroup;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;

import java.util.List;
import java.util.Optional;

public interface IServiceManager extends Startable, Stopable {
    List<IService> getServices();
    Optional<IService> getService(String serviceId);

    void registerServices(IService... iServices);
}
