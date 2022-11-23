package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import io.vertx.core.net.SocketAddress;

import java.util.UUID;

public class Service implements IService {
    private String serviceID;
    private final SocketAddress socketAddress;
    private String[] motd;
    private int maxPlayers;
    private String status;
    private final String serviceGroup;
    private final String javaParams;
    private final ServiceVersion serviceVersion;
    private final String node;
    private final int maxRam;


    public Service(SocketAddress socketAddress, String serviceGroup, String javaParams, ServiceVersion serviceVersion, String node, int maxRam) {
        this.socketAddress = socketAddress;
        this.serviceGroup = serviceGroup;
        this.javaParams = javaParams;
        this.serviceVersion = serviceVersion;
        this.node = node;
        this.maxRam = maxRam;
        this.serviceID = getServiceGroup() + UUID.randomUUID().toString().split("-")[0];
        this.motd = new String[]{"§bAllroundCloud Service","§7by Allround | Julian"};
        this.status = "CREATED";
        this.maxPlayers = 20;
    }

    public Service(SocketAddress socketAddress, String[] motd, int maxPlayers, String status, String serviceGroup, String javaParams, ServiceVersion serviceVersion, String node, int maxRam) {
        this.serviceID = getServiceGroup() + UUID.randomUUID().toString().split("-")[0];
        this.socketAddress = socketAddress;
        this.motd = motd;
        this.maxPlayers = maxPlayers;
        this.status = status;
        this.serviceGroup = serviceGroup;
        this.javaParams = javaParams;
        this.serviceVersion = serviceVersion;
        this.node = node;
        this.maxRam = maxRam;
    }

    @Override
    public int getMaxRam() {
        return maxRam;
    }

    @Override
    public String getNode() {
        return node;
    }

    public ServiceVersion getServiceVersion() {
        return serviceVersion;
    }

    @Override
    public String getJavaParams() {
        return javaParams;
    }


    public int getPort() {
        return this.socketAddress.port();
    }


    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createServiceInfoUpdatePacket());
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public void setMaxPlayers(int i) {
        this.maxPlayers = i;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createServiceInfoUpdatePacket());
    }

    @Override
    public String getServiceID() {
        return serviceID;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    @Override
    public String[] getMotd() {
        return motd;
    }

    @Override
    public ServiceType getType() {
        return null;
    }

    @Override
    public void setMotd(String[] motd) {
        this.motd = motd;
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createServiceInfoUpdatePacket());
    }


    @Override
    public String getServiceGroup() {
        return this.serviceGroup;
    }

    @Override
    public void init() {
        this.serviceID = getServiceGroup() + UUID.randomUUID().toString().split("-")[0];


    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {


    }
}
