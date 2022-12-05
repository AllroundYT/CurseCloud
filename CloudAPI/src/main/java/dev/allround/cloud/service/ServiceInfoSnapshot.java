package dev.allround.cloud.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceInfoSnapshot {
    private final String serviceID,
            motd1,
            motd2,
            status,
            host,
            javaParams,
            startArgs,
            serviceType,
            node,
            groupName,
            serviceVersion;
    private final int port,
            maxPlayers,
            maxRam;

    @Override
    public String toString() {
        return "ServiceInfoSnapshot{" +
                "serviceID='" + serviceID + '\'' +
                ", motd1='" + motd1 + '\'' +
                ", motd2='" + motd2 + '\'' +
                ", status='" + status + '\'' +
                ", host='" + host + '\'' +
                ", javaParams='" + javaParams + '\'' +
                ", startArgs='" + startArgs + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", node='" + node + '\'' +
                ", groupName='" + groupName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", port=" + port +
                ", maxPlayers=" + maxPlayers +
                ", maxRam=" + maxRam +
                '}';
    }
}
