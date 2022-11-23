package dev.allround.cloud.network;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.util.TimeStamp;
import io.vertx.core.net.SocketAddress;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class Packet {
    private final int type_id, type_section;
    private final TimeStamp creationTime;
    private final String[] data;
    private final UUID uuid;
    private int id;
    private UUID sender;
    private UUID requestID;
    private String senderHost;
    private int senderPort;

    public Packet(int type_id, int type_section, String... data) {
        this.type_id = type_id;
        this.type_section = type_section;
        this.creationTime = new TimeStamp();
        this.sender = Cloud.getModule().getModuleInfo().id();
        this.uuid = UUID.randomUUID();
        this.data = data;
    }

    public Packet(PacketType packetType, String... data) {
        if (packetType.needResponse()) this.requestID = UUID.randomUUID();
        this.type_id = packetType.getId();
        this.type_section = packetType.getSection();
        this.uuid = UUID.randomUUID();
        this.sender = Cloud.getModule().getModuleInfo().id();
        this.data = data;
        this.creationTime = new TimeStamp();
    }

    public Packet(int type_id, int type_section, UUID uuid, String... data) {
        this.type_id = type_id;
        this.type_section = type_section;
        this.creationTime = new TimeStamp();
        this.sender = Cloud.getModule().getModuleInfo().id();
        this.data = data;
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public Packet setId(int id) {
        this.id = id;
        return this;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public Packet setSenderPort(int senderPort) {
        this.senderPort = senderPort;
        return this;
    }

    public PacketType getPacketType() {
        return PacketType.get(type_id, type_section);
    }

    public Optional<SocketAddress> getSenderSocket() {
        if (senderHost != null && senderPort != 0) {
            return Optional.of(SocketAddress.inetSocketAddress(senderPort, senderHost));
        }
        return Optional.empty();
    }

    public String getSenderHost() {
        return senderHost;
    }

    public Packet setSenderHost(String senderHost) {
        this.senderHost = senderHost;
        return this;
    }

    public UUID getRequestID() {
        return requestID;
    }

    public Packet setRequestID(UUID requestID) {
        this.requestID = requestID;
        return this;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "type_id=" + type_id +
                ", type_section=" + type_section +
                ", creationTime=" + creationTime +
                ", data=" + Arrays.toString(data) +
                ", id=" + id +
                ", uuid=" + uuid +
                ", sender=" + sender +
                ", requestID=" + requestID +
                ", senderHost='" + senderHost + '\'' +
                ", senderPort=" + senderPort +
                '}';
    }

    public UUID getSender() {
        return sender;
    }

    public Packet setSender(UUID sender) {
        this.sender = sender;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getType_id() {
        return type_id;
    }

    public int getType_section() {
        return type_section;
    }

    public TimeStamp getCreationTime() {
        return creationTime;
    }

    public String[] getData() {
        return data;
    }
}
