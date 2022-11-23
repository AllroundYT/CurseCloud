package dev.allround.cloud.network;

import com.google.gson.Gson;

public class PacketSerializer {
    private final Gson gson = new Gson();

    public String serialize(Packet packet) {
        return this.gson.toJson(packet);
    }

    public Packet deserialize(String s) {
        return this.gson.fromJson(s, Packet.class);
    }
}
