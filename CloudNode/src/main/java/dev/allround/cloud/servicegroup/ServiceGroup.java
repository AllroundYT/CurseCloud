package dev.allround.cloud.servicegroup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.exceptions.ServiceJarNotFoundException;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.*;
import dev.allround.cloud.util.NodeProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ServiceGroup implements IServiceGroup{

    private final ServiceType type;
    private final String node;
    private int minOnlineAmount;
    private int maxOnlineAmount;
    private int maxPlayers;
    private final String groupName;
    private int maxRam;
    private double percentageToStartNewService;
    private final ServiceVersion serviceVersion;
    private final String javaParams;
    private final String startArgs;

    @Override
    public String getStartArgs() {
        return startArgs;
    }

    public String getJavaParams() {
        return javaParams;
    }

    public ServiceGroup(ServiceType type, String node, String groupName, ServiceVersion serviceVersion) {
        this.type = type;
        this.node = node;
        this.groupName = groupName;
        this.serviceVersion = serviceVersion;
        this.minOnlineAmount = 1;
        this.maxOnlineAmount = 1;
        this.maxRam = 1024;
        this.maxPlayers = 20;
        this.percentageToStartNewService = 0.75d;
        this.javaParams = "-Dcloud.network.host=%HOST% -Dcloud.network.port=%PORT%"
                .replace("%HOST%",Cloud.getModule().getComponent(NodeProperties.class).getNetworkServerHost())
                .replace("%PORT%",String.valueOf(Cloud.getModule().getComponent(NodeProperties.class).getMaxServerPort()))
        ;
        this.startArgs = "nogui";
    }

    @Override
    public void update() {
        if (!Cloud.getWrapper().isThisModule(getNode())){
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(PacketType.API_UPDATE_SERVICE_GROUP,new String[]{getGroupName()});
            return;
        }

        while (getOnlineServiceAmount() > getMaxOnlineAmount()) {
            getServices(IService::isOnline).stream().sorted(Comparator.comparingInt(value -> value.getPlayers().size())).toList().get(0).setStatus("BLOCKED");
        }

        while (getOnlineServiceAmount() < getMinOnlineAmount() && getOnlineServiceAmount() < getMaxOnlineAmount() ){
            Service service = new Service(this);
            service.start();
        }
    }

    @Override
    public boolean needNewService() {
        IService iService = getServiceWithLowestPlayerAmount();
        int players = iService.getPlayers().size();
        int maxPlayers = iService.getMaxPlayers();
        return ((((double) players / ((double) maxPlayers / 100d)) / 100d) >= percentageToStartNewService);
    }

    @Override
    public boolean updateTemplate(boolean printWarnMsg) {
        if (!Cloud.getWrapper().isThisModule(getNode())){
            return false;
        }
        try {
            Path templatePath = Path.of("templates", getGroupName());
            Path pluginsPath = Path.of(templatePath.toAbsolutePath().toString(),"plugins");
            Path serverJarPath;
            if (getType() == ServiceType.PROXY){
                serverJarPath = Path.of(templatePath.toAbsolutePath().toString(),"proxy.jar");
            }else {
                serverJarPath = Path.of(templatePath.toAbsolutePath().toString(),"server.jar");
            }

            if (Files.notExists(templatePath)) Files.createDirectories(templatePath);
            if (Files.notExists(pluginsPath)) Files.createDirectories(pluginsPath);
            if (Files.notExists(serverJarPath)) throw new ServiceJarNotFoundException(this);

            return true;
        } catch (IOException e){
            Cloud.getModule().getCloudLogger().error(e);
            return false;
        } catch (ServiceJarNotFoundException e) {
            if (printWarnMsg) Cloud.getModule().getCloudLogger().warn(e.getMessage());
            return false;
        }
    }

}
