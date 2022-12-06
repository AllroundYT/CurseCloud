package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.PacketType;
import io.vertx.core.net.SocketAddress;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetSocketAddress;

@AllArgsConstructor
public class Service implements IService {
    private final String node;
    private final SocketAddress socketAddress;
    private final ServiceType type;
    private final ServiceVersion serviceVersion;
    private final String serviceGroup;
    private final String serviceID;
    private final String javaParams;
    private final int maxRam;
    private final String startArgs;
    private String[] motd;
    private String status;
    private int maxPlayers;
    private Process process;

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public void setProcess(Process process) {
        this.process = process;
    }


    @Override
    public String getStartArgs() {
        return startArgs;
    }

    @Override
    public boolean copyTemplate(boolean printWarnMsg) {
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
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createServiceInfoUpdatePacket());
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
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createServiceInfoUpdatePacket());
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
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(createServiceInfoUpdatePacket());
    }


    @Override
    public void start() {
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(PacketType.API_START_SERVICE_OF_GROUP, new String[]{getServiceID()});
    }

    @Override
    public void cloneServiceInfo(IService service) {
        this.status = service.getStatus();
        this.maxPlayers = service.getMaxPlayers();
        this.motd = service.getMotd();
    }

    @Override
    public void stop() { //INFO: Service wird entregistriert wenn PacketType.SERVICE_STOP packet empfangen wird
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(PacketType.API_STOP_SERVICE, new String[]{getServiceID()});
    }
}
