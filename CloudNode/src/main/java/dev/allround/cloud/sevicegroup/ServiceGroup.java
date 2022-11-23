package dev.allround.cloud.sevicegroup;

import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.service.ServiceVersion;
import dev.allround.cloud.servicegroup.IServiceGroup;

public class ServiceGroup implements IServiceGroup {
    private final String node;
    private int maxRam;
    private int maxPlayers;
    private final String groupName;
    private int minOnlineAmount;
    private int maxOnlineAmount;
    private double percentageToStartNewService;
    private final ServiceVersion serviceVersion;

    @Override
    public ServiceVersion getServiceVersion() {
        return serviceVersion;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public ServiceGroup(String node, String groupName, ServiceVersion serviceVersion) {
        this.node = node;
        this.groupName = groupName;
        this.serviceVersion = serviceVersion;
    }

    public ServiceGroup(String node, int maxRam, int maxPlayers, String groupName, int minOnlineAmount, int maxOnlineAmount, double percentageToStartNewService, ServiceVersion serviceVersion) {
        this.node = node;
        this.maxRam = maxRam;
        this.maxPlayers = maxPlayers;
        this.groupName = groupName;
        this.minOnlineAmount = minOnlineAmount;
        this.maxOnlineAmount = maxOnlineAmount;
        this.percentageToStartNewService = percentageToStartNewService;
        this.serviceVersion = serviceVersion;
    }

    @Override
    public int getMaxRam() {
        return maxRam;
    }

    @Override
    public ServiceType getType() {
        return null;
    }

    @Override
    public String getNode() {
        return node;
    }

    @Override
    public void setMaxRam(int maxRam) {
        this.maxRam = maxRam;
    }

    @Override
    public void setMaxOnlineAmount(int maxOnlineAmount) {
        this.maxOnlineAmount = maxOnlineAmount;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void setMinOnlineAmount(int minOnlineAmount) {
        this.minOnlineAmount = minOnlineAmount;
    }

    @Override
    public void setPercentageToStartNewService(double percentageToStartNewService) {
        this.percentageToStartNewService = percentageToStartNewService;
    }

    @Override
    public double getPercentageToStartNewService() {
        return percentageToStartNewService;
    }


    @Override
    public int getMaxOnlineAmount() {
        return maxOnlineAmount;
    }

    @Override
    public int getMinOnlineAmount() {
        return minOnlineAmount;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }


    @Override
    public boolean needNewService() {
        return false;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
