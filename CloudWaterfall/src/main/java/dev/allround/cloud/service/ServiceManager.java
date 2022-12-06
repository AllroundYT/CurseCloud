package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.util.FileUtils;
import dev.allround.cloud.util.Stopable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ServiceManager implements IServiceManager{

    private final Set<IService> services;

    public ServiceManager() {
        this.services = new HashSet<>();
    }

    @Override
    public Set<IService> getServices() {
        return services;
    }

    @Override
    public Optional<IService> getService(String serviceId) {
        return getServices().stream().filter(service -> service.getServiceID().equals(serviceId)).findFirst();
    }

    @Override
    public void registerServices(IService... iServices){
        this.services.addAll(List.of(iServices));
    }

    @Override
    public void unregisterServices(IService... iServices) {
        synchronized (this.services){
            List.of(iServices).forEach(this.services::remove);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void update(IService iService) {
        if (getService(iService.getServiceID()).isEmpty()){
            getServices().add(iService);
        }else {
            getService(iService.getServiceID()).get().cloneServiceInfo(iService);
        }
    }

    @Override
    public void queueStart(IService iService){ //INFO: Wird nur beim Main node genutzt
    }

    @Override
    public void stop() {
    }
}
