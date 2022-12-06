package dev.allround.cloud.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ServiceManager implements IServiceManager {

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
    public void registerServices(IService... iServices) {
        this.services.addAll(List.of(iServices));
    }

    @Override
    public void unregisterServices(IService... iServices) {
        synchronized (this.services) {
            List.of(iServices).forEach(this.services::remove);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void update(IService iService) {
        if (getService(iService.getServiceID()).isEmpty()) {
            getServices().add(iService);
        } else {
            getService(iService.getServiceID()).get().cloneServiceInfo(iService);
        }
    }

    @Override
    public void queueStart(IService iService) { //INFO: Wird nur beim Main node genutzt
    }

    @Override
    public void stop() {
    }
}
