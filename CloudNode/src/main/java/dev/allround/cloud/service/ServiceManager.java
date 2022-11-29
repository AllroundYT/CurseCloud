package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.util.NodeProperties;
import dev.allround.cloud.util.process.ProcessPool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ServiceManager implements IServiceManager{

    private final List<IService> services;

    public ServiceManager() {
        this.services = new ArrayList<>();
        this.startQueue = new HashSet<>();
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

    private final HashSet<IService> startQueue;

    @Override
    public void queueStart(IService iService){ //TODO: nur noch auf linux nutzt bar
        startQueue.add(iService);
        Cloud.getModule().getScheduledExecutorService().scheduleAtFixedRate(() -> {
            startQueue.forEach(service -> {
                if (service.copyTemplate(false)){
                    ProcessBuilder processBuilder = new ProcessBuilder().directory(Path.of("temp", service.getServiceID()).toFile()).command("cmd.exe", "/c", "java -Xmx" + service.getMaxRam() + "M -Xms" + service.getMaxRam() + "M -Dcloud.network.host=" + Cloud.getModule().getComponent(NodeProperties.class).getNetworkServerHost() + " -Dcloud.network.port=" + Cloud.getModule().getComponent(NodeProperties.class).getNetworkServerPort() + " " + service.getJavaParams() + " -jar " + (service.getType() == ServiceType.PROXY ? "proxy.jar" : "server.jar") + " "+service.getStartArgs());
                    try {
                        Cloud.getModule().getCloudLogger().info("Try starting "+iService.getServiceID()+":"+iService.getNode()+"...");
                        service.setProcess(processBuilder.start());
                    } catch (IOException e) {
                        Cloud.getModule().getCloudLogger().error(e);
                    }
                    startQueue.remove(service);
                }
            });
        },0,5,TimeUnit.SECONDS);
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
