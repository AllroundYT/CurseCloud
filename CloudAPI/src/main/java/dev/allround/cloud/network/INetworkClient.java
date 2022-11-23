package dev.allround.cloud.network;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.util.Stopable;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

public abstract class INetworkClient implements Stopable {
    private final HashMap<UUID, Consumer<Packet>[]> waitingForResponse;
    private final PacketSerializer packetSerializer;
    private final INetworkManager manager;
    private final RecordParser recordParser;
    private final LinkedList<Packet> packetsToSend;
    private final LinkedList<UUID> sentPacketIds;
    private NetSocket netSocket;
    private NetClient netClient;

    public INetworkClient(INetworkManager manager) {
        this.packetSerializer = manager.getPacketSerializer();
        this.manager = manager;
        this.waitingForResponse = new HashMap<>();
        this.recordParser = RecordParser.newDelimited("\n", data -> {
            onDataReceive(data.toString());
        });
        this.packetsToSend = new LinkedList<>();
        this.sentPacketIds = new LinkedList<>();
    }

    public Future<?> startClient(InetSocketAddress address) {

        this.netClient = Cloud.getModule().getComponent(Vertx.class).createNetClient();

        return netClient.connect(address.getPort(), address.getAddress().getHostAddress()).onSuccess(netSocket -> {
            this.netSocket = netSocket;
            netSocket.handler(this.recordParser);
            netSocket.closeHandler(unused -> Cloud.getModule().getCloudLogger().info("[Network - Client] NetSocket closed!"));
            netSocket.exceptionHandler(throwable -> {
                Cloud.getModule().getCloudLogger().error("[Network - Client] NetSocket has thrown an exception.");
                Cloud.getModule().getCloudLogger().error(throwable);
            });
            Cloud.getModule().getCloudLogger().info("[Network - Client] NetSocket ready!");
            while (this.packetsToSend.size() > 0) {
                writePacket(this.packetsToSend.remove());
            }
            onConnectionSuccess(this.netSocket);
        }).onFailure(throwable -> {
            Cloud.getModule().getCloudLogger().error("[Network - Client] NetSocket failed starting process! (Did you start the main node?)");
            Cloud.getModule().getCloudLogger().info("[Network - Client] Use the following command to restart this node: restart");
            Cloud.getModule().getCloudLogger().silentError(throwable);
        });

    }

    public abstract void onConnectionSuccess(NetSocket netSocket);


    public Optional<NetSocket> getNetSocket() {
        return Optional.ofNullable(this.netSocket);
    }

    public Optional<NetClient> getNetClient() {
        return Optional.ofNullable(this.netClient);
    }

    @SafeVarargs
    public final void sendPacket(Packet packet, Consumer<Packet>... onResponse) {
        assert getNetSocket().isPresent();
        if (Objects.requireNonNull(PacketType.get(packet.getType_id(), packet.getType_section())).needResponse()) {
            waitingForResponse.put(packet.getRequestID(), onResponse);
        }
        writePacket(packet);
        this.sentPacketIds.add(packet.getUuid());
    }

    public void onDataReceive(String data) {
        Packet packet = this.packetSerializer.deserialize(data);
        if (this.sentPacketIds.contains(packet.getUuid())) return;
        if (waitingForResponse.containsKey(packet.getRequestID())) {
            Arrays.stream(waitingForResponse.get(packet.getRequestID())).forEach(packetConsumer -> packetConsumer.accept(packet));
        }
        Cloud.getModule().getCloudLogger().debug("[Network - Client] Packet received -> " + packet + " Delay: " + Duration.between(packet.getCreationTime().getInstant(), Instant.now()).toMillis() + "ms");
        manager.onPacket(packet);
    }

    private void writePacket(Packet packet) {
        if (getNetSocket().isEmpty()) {
            this.packetsToSend.add(packet);
            return;
        } else netSocket.write(this.packetSerializer.serialize(packet) + "\n");
        Cloud.getModule().getCloudLogger().debug("[Network - Client] Packet sent -> " + packet.toString());
    }


    public void stop() {
        if (getNetClient().isPresent()) {
            getNetClient().get().close().onFailure(throwable -> Cloud.getModule().getCloudLogger().error(throwable)).onSuccess(unused -> Cloud.getModule().getCloudLogger().info("[Network - Client] Closed!"));
        }
    }
}
