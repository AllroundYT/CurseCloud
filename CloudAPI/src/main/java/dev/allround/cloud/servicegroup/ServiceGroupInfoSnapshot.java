package dev.allround.cloud.servicegroup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceGroupInfoSnapshot {
    private final String javaParams,startArgs,serviceType,node,groupName,serviceVersion;
    private final int minOnlineAmount,maxOnlineAmount,maxPlayers,maxRam;
    private final double percentageToStartNewService;

    @Override
    public String toString() {
        return "ServiceGroupInfoSnapshot{" +
                "javaParams='" + javaParams + '\'' +
                ", startArgs='" + startArgs + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", node='" + node + '\'' +
                ", groupName='" + groupName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", minOnlineAmount=" + minOnlineAmount +
                ", maxOnlineAmount=" + maxOnlineAmount +
                ", maxPlayers=" + maxPlayers +
                ", maxRam=" + maxRam +
                ", percentageToStartNewService=" + percentageToStartNewService +
                '}';
    }
}
