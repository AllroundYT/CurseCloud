package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.util.FileUtils;
import dev.allround.cloud.util.NodeProperties;
import dev.allround.cloud.util.Stopable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ServiceManager implements IServiceManager{

    private final Set<IService> services;

    public ServiceManager() {
        this.services = new HashSet<>();
        this.startQueue = new HashSet<>();
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
        Cloud.getModule().getScheduledExecutorService().scheduleAtFixedRate(() -> {
            //Alle 5 Minuten werden temp dateien von nicht mehr benötigten servern gelöscht.
            Path tempPath = Path.of("temp");

            for (String file : Objects.requireNonNull(tempPath.toFile().list((dir, name) -> !name.contains(".") && getService(name).isEmpty()))) {
                FileUtils.delete(Path.of("temp",file).toFile());
            }
        },0,5, TimeUnit.MINUTES);
    }

    private final HashSet<IService> startQueue;

    @Override
    public void update(IService iService) {
        if (getService(iService.getServiceID()).isEmpty()){
            getServices().add(iService);
        }else {
            getService(iService.getServiceID()).get().cloneServiceInfo(iService);
        }
    }

    @Override
    public void queueStart(IService iService){ //INFO: nur noch auf linux nutzt bar
        startQueue.add(iService);
        Cloud.getModule().getScheduledExecutorService().scheduleAtFixedRate(() -> {
            startQueue.forEach(service -> {
                if (service.copyTemplate(false)){
                    //ProcessBuilder processBuilder = new ProcessBuilder().directory(Path.of("temp", service.getServiceID()).toFile()).command("/bin/sh", "-c", "screen -s "+iService.getServiceID()+"  /bin/sh -c  java -Xmx" + service.getMaxRam() + "M -Xms" + service.getMaxRam() + "M -Dcloud.network.host=" + Cloud.getModule().getComponent(NodeProperties.class).getNetworkServerHost() + " -Dcloud.network.port=" + Cloud.getModule().getComponent(NodeProperties.class).getNetworkServerPort() + " " + service.getJavaParams() + " -jar " + (service.getType() == ServiceType.PROXY ? "proxy.jar" : "server.jar") + " "+service.getStartArgs());
                    //try {
                        Cloud.getModule().getCloudLogger().info("Try starting "+iService.getServiceID()+":"+iService.getNode()+"...");
                        //service.setProcess(processBuilder.start());
                        Cloud.getModule().getCloudLogger().info("Service started in screen \""+iService.getServiceID()+"\"");
                        iService.setStatus("PROCESS_STARTED");
                        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(iService.createServiceInfoUpdatePacket());
                    /*} catch (IOException e) {
                        Cloud.getModule().getCloudLogger().error(e);
                    }

                     */
                    startQueue.remove(service);
                }
            });
        },0,5,TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        if (!Cloud.getModule().getComponent(NodeProperties.class).isMainNode()) return;
        synchronized (getServices()){
            getServices().forEach(Stopable::stop);
        }
    }
}
