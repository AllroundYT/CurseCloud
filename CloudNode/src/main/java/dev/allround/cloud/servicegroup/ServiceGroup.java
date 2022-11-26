package dev.allround.cloud.servicegroup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.IService;
import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.service.ServiceVersion;
import dev.allround.cloud.util.NodeProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

        while ((needNewService() || getOnlineServiceAmount() < getMinOnlineAmount()) && getOnlineServiceAmount() < getMaxOnlineAmount() ){
            //TODO: starte neuen server dieser gruppe
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
    public void updateTemplate() {

    }

}
