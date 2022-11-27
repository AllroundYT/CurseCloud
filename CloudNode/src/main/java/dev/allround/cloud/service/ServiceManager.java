package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.util.NodeProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ServiceManager implements IServiceManager{

    private final List<IService> services;

    public ServiceManager() {
        this.services = new ArrayList<>();
    }

    @Override
    public List<IService> getServices() {
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
    public void start() {
        Cloud.getModule().getScheduledExecutorService().scheduleAtFixedRate(() -> {
            //Alle 5 Minuten werden temp dateien von nicht mehr benötigten servern gelöscht.
            try {
                Path tempPath = Path.of("temp");

                for (String file : Objects.requireNonNull(tempPath.toFile().list((dir, name) -> !name.contains(".") && getService(name).isEmpty()))) {
                    Files.deleteIfExists(Path.of("temp", file));
                }
            }catch (IOException e){
                Cloud.getModule().getCloudLogger().error(e);
            }
        },0,5, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {
        if (!Cloud.getModule().getComponent(NodeProperties.class).isMainNode()) return;
        getServices().forEach(service -> {
            service.stop();
            this.services.remove(service);
        });
    }
}
