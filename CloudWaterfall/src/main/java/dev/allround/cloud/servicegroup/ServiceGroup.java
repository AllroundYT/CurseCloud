package dev.allround.cloud.servicegroup;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.IService;
import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.service.ServiceVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceGroup implements IServiceGroup {

    private final ServiceType type;
    private final String node;
    private final String groupName;
    private final ServiceVersion serviceVersion;
    private final String javaParams;
    private final String startArgs;
    private int minOnlineAmount;
    private int maxOnlineAmount;
    private int maxPlayers;
    private int maxRam;
    private double percentageToStartNewService;

    @Override
    public String getStartArgs() {
        return startArgs;
    }

    public String getJavaParams() {
        return javaParams;
    }

    public void setMinOnlineAmount(int minOnlineAmount) {
        this.minOnlineAmount = minOnlineAmount;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createGroupInfoUpdatePacket());
    }

    public void setMaxOnlineAmount(int maxOnlineAmount) {
        this.maxOnlineAmount = maxOnlineAmount;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createGroupInfoUpdatePacket());
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createGroupInfoUpdatePacket());
    }

    public void setMaxRam(int maxRam) {
        this.maxRam = maxRam;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createGroupInfoUpdatePacket());
    }

    public void setPercentageToStartNewService(double percentageToStartNewService) {
        this.percentageToStartNewService = percentageToStartNewService;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createGroupInfoUpdatePacket());
    }


    @Override
    public void cloneGroupInfo(IServiceGroup serviceGroup) {
        this.minOnlineAmount = serviceGroup.getMinOnlineAmount();
        this.maxPlayers = serviceGroup.getMaxPlayers();
        this.maxOnlineAmount = serviceGroup.getMaxOnlineAmount();
        this.maxRam = serviceGroup.getMaxRam();
        this.percentageToStartNewService = serviceGroup.getPercentageToStartNewService();
    }

    @Override
    public void update() {
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(PacketType.API_UPDATE_SERVICE_GROUP, new String[]{getGroupName()});
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
        return true;
    }

}
