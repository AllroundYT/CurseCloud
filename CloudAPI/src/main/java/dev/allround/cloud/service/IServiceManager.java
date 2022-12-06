package dev.allround.cloud.service;

import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;

import java.util.Optional;
import java.util.Set;

public interface IServiceManager extends Startable, Stopable {
    Set<IService> getServices();

    Optional<IService> getService(String serviceId);

    void registerServices(IService... iServices);

    void unregisterServices(IService... iServices);

    void queueStart(IService iService);

    void update(IService iService);
}
