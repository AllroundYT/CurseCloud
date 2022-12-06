package dev.allround.cloud.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CloudPlayerInfoSnapshot {
    private final UUID uuid;
    private final String name, proxy, service;
    private final boolean operator, online;
    private final String[] data;

    @Override
    public String toString() {
        return "CloudPlayerInfoSnapshot{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", proxy='" + proxy + '\'' +
                ", service='" + service + '\'' +
                ", operator=" + operator +
                ", online=" + online +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
