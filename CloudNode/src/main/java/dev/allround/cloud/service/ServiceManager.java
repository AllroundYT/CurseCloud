package dev.allround.cloud.service;

import dev.allround.cloud.servicegroup.IServiceGroup;

import java.util.List;
import java.util.Optional;

public class ServiceManager implements IServiceManager {
    @Override
    public List<IService> getServices() {
        return null;
    }

    @Override
    public List<IService> createServices(IServiceGroup iServiceGroup, int amount) {
        return null;
    }

    @Override
    public IService createService(IServiceGroup iServiceGroup) {
        return null;
    }


    @Override
    public Optional<IService> getService(String serviceId) {
        return Optional.empty();
    }

    @Override
    public List<IService> getStartList() {
        return null;
    }

    @Override
    public List<IService> getStopList() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
