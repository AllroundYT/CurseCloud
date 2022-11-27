package dev.allround.cloud.servicegroup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.service.ServiceVersion;
import dev.allround.cloud.util.Document;
import dev.allround.cloud.util.NodeProperties;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Getter
public class ServiceGroupManager implements IServiceGroupManager{
    private final List<IServiceGroup> serviceGroups;

    public ServiceGroupManager() {
        this.serviceGroups = new ArrayList<>();
    }

    @Override
    public Optional<IServiceGroup> getServiceGroup(String name) {
        return getServiceGroups().stream().filter(iServiceGroup -> iServiceGroup.getGroupName().equals(name)).findFirst();
    }

    @Override
    public void loadThisModulesGroups() {
        try (final Stream<Path> pathStream = Files.list(Path.of("data","groups"))){
            pathStream.forEach(path -> {
                try {
                    if (path.toString().endsWith(".json")) {
                        Document document = new Document(path);
                        String node, type, version, name, javaParams;
                        double percentageToStartNewService;
                        int minOnlineAmount, maxOnlineAmount, maxRam, maxPlayers;

                        name = document.get("name", String.class);
                        node = document.get("node", String.class);
                        type = document.get("type", String.class);
                        version = document.get("version", String.class);
                        javaParams = document.get("javaParams",String.class);

                        percentageToStartNewService = document.get("percentageToStartNewService", Double.class);

                        minOnlineAmount = document.get("minOnlineAmount", Integer.class);
                        maxOnlineAmount = document.get("maxOnlineAmount", Integer.class);
                        maxRam = document.get("maxRam", Integer.class);
                        maxPlayers = document.get("maxPlayers", Integer.class);

                        ServiceType serviceType = ServiceType.valueOf(type);
                        ServiceVersion serviceVersion = ServiceVersion.valueOf(version);

                        ServiceGroup serviceGroup = new ServiceGroup(serviceType,node,minOnlineAmount,maxOnlineAmount,maxPlayers,name,maxRam,percentageToStartNewService,serviceVersion,javaParams);
                        this.registerServiceGroup(serviceGroup);
                    }
                }catch (Exception ignored){}
            });
        } catch (IOException e) {
            Cloud.getModule().getCloudLogger().error(e);
        }
    }

    @Override
    public void saveThisModulesGroups() {
        getServiceGroups().forEach(iServiceGroup -> {
            Document document = new Document();
            document.set("name",iServiceGroup.getGroupName())
                    .set("node",iServiceGroup.getNode())
                    .set("minOnlineAmount",iServiceGroup.getMinOnlineAmount())
                    .set("maxOnlineAmount",iServiceGroup.getMaxOnlineAmount())
                    .set("maxRam",iServiceGroup.getMaxRam())
                    .set("maxPlayers",iServiceGroup.getMaxPlayers())
                    .set("type",iServiceGroup.getType().name())
                    .set("version",iServiceGroup.getServiceVersion().name())
                    .set("percentageToStartNewService",iServiceGroup.getPercentageToStartNewService())
                    .set("javaParams",iServiceGroup.getJavaParams())
            ;
            Path groupFilePath = Path.of("data","groups",iServiceGroup.getGroupName()+".json");
            try {
                Files.deleteIfExists(groupFilePath);
            } catch (IOException e){
                Cloud.getModule().getCloudLogger().error(e);
            }
            document.write(groupFilePath);
        });
    }

    @Override
    public void updateGroups(IServiceGroup... iServiceGroups) {
        for (IServiceGroup iServiceGroup : iServiceGroups) {
            iServiceGroup.update();
        }
    }

    private void startUpdateSchedule(){
        if (!Cloud.getModule().getComponent(NodeProperties.class).isMainNode()) return;
        Cloud.getModule().getScheduledExecutorService().scheduleAtFixedRate(() -> {
            getServiceGroups().forEach(IServiceGroup::update);
            System.out.println("GROUP UPDATE");
        }, 0,20, TimeUnit.SECONDS);
    }

    @Override
    public void start() {
        if (!Cloud.getModule().getComponent(NodeProperties.class).isMainNode()) return;
        loadThisModulesGroups();
        startUpdateSchedule();
    }

    @Override
    public void stop() {
        if (!Cloud.getModule().getComponent(NodeProperties.class).isMainNode()) return;
        saveThisModulesGroups();
    }
}
