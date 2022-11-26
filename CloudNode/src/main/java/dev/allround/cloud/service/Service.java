package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.servicegroup.IServiceGroup;
import dev.allround.cloud.servicegroup.IServiceGroupManager;
import dev.allround.cloud.util.FileUtils;
import dev.allround.cloud.util.NodeProperties;
import dev.allround.cloud.util.PortChecker;
import io.vertx.core.net.SocketAddress;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class Service implements IService {
    private final String node;
    private final SocketAddress socketAddress;
    private final ServiceType type;
    private final ServiceVersion serviceVersion;
    private final String serviceGroup;
    private final String serviceID;
    private final String javaParams;
    private String[] motd;
    private String status;
    private final int maxRam;
    private int maxPlayers;

    public Service(String node, SocketAddress socketAddress, ServiceType type, ServiceVersion serviceVersion, String serviceGroup, String serviceID, String javaParams, int maxRam) {
        this.node = node;
        this.socketAddress = socketAddress;
        this.type = type;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.serviceID = serviceID;
        this.javaParams = javaParams;
        this.maxRam = maxRam;
        this.maxPlayers = 20;
        this.status = "CREATED";
        this.motd = new String[]{"§6Service by AllroundCloud","§7by Allround | Julian"};
    }

    public Service(String node, String[] motd, String host, int port, ServiceType type, ServiceVersion serviceVersion, int maxRam, String serviceGroup, String serviceID, int maxPlayers, String javaParams) {
        this.node = node;
        this.motd = motd;
        this.socketAddress = SocketAddress.inetSocketAddress(port,host);
        this.type = type;
        this.serviceVersion = serviceVersion;
        this.status = "CREATED";
        this.maxRam = maxRam;
        this.serviceGroup = serviceGroup;
        this.serviceID = serviceID;
        this.maxPlayers = maxPlayers;
        this.javaParams = javaParams;
    }

    @SneakyThrows
    public Service(IServiceGroup iServiceGroup){
        this.node = iServiceGroup.getNode();
        this.socketAddress = SocketAddress.inetSocketAddress(PortChecker.getFreePort(
                Cloud.getModule().getComponent(NodeProperties.class).getMinServerPort(),
                Cloud.getModule().getComponent(NodeProperties.class).getMaxServerPort(),
                Cloud.getModule().getComponent(NodeProperties.class).getMinServerPort()
        ),InetAddress.getLocalHost().getHostAddress());
        this.type = iServiceGroup.getType();
        this.serviceVersion = iServiceGroup.getServiceVersion();
        this.serviceGroup = iServiceGroup.getGroupName();
        this.serviceID = getServiceGroup() + "-" + UUID.randomUUID().toString().split("-")[0];
        this.javaParams = iServiceGroup.getJavaParams();
        this.maxRam = iServiceGroup.getMaxRam();
        this.maxPlayers = iServiceGroup.getMaxPlayers();
        this.status = "CREATED";
        this.motd = new String[]{"§6Service by AllroundCloud","§7by Allround | Julian"};
    }
    @Override
    public boolean copyTemplate() {
        if (!Cloud.getWrapper().isThisModule(getNode())) return false;
        if (Cloud.getModule().getComponent(IServiceGroupManager.class).getServiceGroup(getServiceGroup()).isEmpty()) return false;
        Cloud.getModule().getComponent(IServiceGroupManager.class).getServiceGroup(getServiceGroup()).get().updateTemplate();

        Path templatePath = Path.of("templates",getServiceGroup());
        Path tempPath = Path.of("temp",getServiceID());

        if (Files.notExists(templatePath)) return false;

        try {
            Files.copy(templatePath,tempPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Cloud.getModule().getCloudLogger().error(e);
            return false;
        }

        return true;
    }

    @Override
    public String getNode() {
        return node;
    }

    @Override
    public String[] getMotd() {
        return motd;
    }

    @Override
    public void setMotd(String[] motd) {
        this.motd = motd;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    @Override
    public ServiceType getType() {
        return type;
    }

    @Override
    public ServiceVersion getServiceVersion() {
        return serviceVersion;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getServiceID() {
        return serviceID;
    }

    @Override
    public int getMaxRam() {
        return maxRam;
    }

    @Override
    public String getServiceGroup() {
        return serviceGroup;
    }

    @Override
    public String getJavaParams() {
        return javaParams;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void init() {
        if (!Cloud.getWrapper().isThisModule(getNode())) return;
        copyTemplate();
    }

    @Override
    public void start() {
        if (!Cloud.getWrapper().isThisModule(getNode())) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(PacketType.API_START_SERVICE,new String[]{getServiceID()});
            return;
        }
        init();
        Cloud.getModule().getComponent(IServiceManager.class).queueStart(this);
    }

    @Override
    public void stop() {
        if (!Cloud.getWrapper().isThisModule(getNode())) {
            Cloud.getModule().getComponent(INetworkClient.class).sendPacket(PacketType.API_STOP_SERVICE,new String[]{getServiceID()});
            return;
        }
        Cloud.getModule().getComponent(IServiceManager.class).queueStop(this);
    }
}
