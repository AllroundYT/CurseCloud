package dev.allround.cloud.network;

import com.google.gson.Gson;

import java.util.Base64;

public class PacketSerializer {
    private final Gson gson = new Gson();

    public String serialize(Packet packet) {
        return Base64.getEncoder().encodeToString(this.gson.toJson(packet).getBytes());
    }

    public Packet deserialize(String s) {
        return this.gson.fromJson(new String(Base64.getDecoder().decode(s.getBytes())), Packet.class);
    }
}
