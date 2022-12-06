package dev.allround.cloud.servicegroup;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.player.ICloudPlayer;
import dev.allround.cloud.player.IPlayerManager;
import dev.allround.cloud.service.IService;
import dev.allround.cloud.service.IServiceManager;
import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.service.ServiceVersion;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IServiceGroup {
    default int getOnlineServiceAmount() {
        return getServices().stream().filter(service -> service.isOnline() || service.isStarting()).toList().size();
    }

    default List<IService> getServices() {
        return Cloud.getModule().getComponent(IServiceManager.class)
                .getServices()
                .stream()
                .filter(iService -> iService.getServiceGroup().equals(this.getGroupName()))
                .collect(Collectors.toList());
    }

    String getJavaParams();

    default List<IService> getServices(Predicate<IService> predicate) {
        return Cloud.getModule().getComponent(IServiceManager.class)
                .getServices()
                .stream()
                .filter(iService -> iService.getServiceGroup().equals(this.getGroupName()))
                .filter(predicate)
                .collect(Collectors.toList());
    }

    default IService getServiceWithLowestPlayerAmount() {
        return getServices().stream().sorted(Comparator.comparingInt(service0 -> service0.getPlayers().size())).toList().get(0);
    }

    ServiceType getType();

    String getNode();

    void update();

    default List<ICloudPlayer> getPlayers() {
        return Cloud.getModule().getComponent(IPlayerManager.class)
                .getCloudPlayers()
                .stream()
                .filter(iCloudPlayer ->
                        Cloud.getModule().getComponent(IServiceManager.class)
                                .getService(iCloudPlayer.getService())
                                .get().getServiceGroup().equals(this.getGroupName()))
                .collect(Collectors.toList());
    }

    int getMinOnlineAmount();

    void setMinOnlineAmount(int amount);

    int getMaxOnlineAmount();

    void setMaxOnlineAmount(int amount);

    String getGroupName();

    int getMaxRam();

    void setMaxRam(int amount);

    int getMaxPlayers();

    void setMaxPlayers(int maxPlayers);

    double getPercentageToStartNewService();

    void setPercentageToStartNewService(double amount);

    ServiceVersion getServiceVersion();

    void cloneGroupInfo(IServiceGroup serviceGroup);

    default Packet createGroupInfoUpdatePacket() {
        return new Packet(
                PacketType.GROUP_INFO_UPDATED,
                new Gson().toJson(
                        new ServiceGroupInfoSnapshot(
                                getJavaParams(),
                                getStartArgs(),
                                getType().name(),
                                getNode(),
                                getGroupName(),
                                getServiceVersion().name(),
                                getMinOnlineAmount(),
                                getMaxOnlineAmount(),
                                getMaxPlayers(),
                                getMaxRam(),
                                getPercentageToStartNewService()
                        )
                )
        );
        /*return new Packet(
                PacketType.GROUP_INFO_UPDATE,
                getGroupName(),
                getNode(),
                getType().name(),
                getServiceVersion().name(),
                String.valueOf(getMinOnlineAmount()),
                String.valueOf(getMaxOnlineAmount()),
                String.valueOf(getMaxRam()),
                String.valueOf(getPercentageToStartNewService()),
                getStartArgs(),
                getJavaParams(),
                String.valueOf(getMaxPlayers())
                );

         */
    }

    boolean needNewService();

    boolean updateTemplate(boolean printWarnMsg);

    String getStartArgs();
}
