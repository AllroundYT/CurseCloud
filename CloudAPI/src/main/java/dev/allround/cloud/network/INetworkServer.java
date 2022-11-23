package dev.allround.cloud.network;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleType;
import dev.allround.cloud.util.PortChecker;
import dev.allround.cloud.util.Stopable;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.parsetools.RecordParser;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class INetworkServer implements Stopable {

    private final PacketSerializer packetSerializer;
    private final List<CloudSocket> connectedSockets;
    private final INetworkManager manager;
    private final HashMap<UUID, Consumer<Packet>[]> waitingForResponse;
    private final RecordParser recordParser;
    private final LinkedHashMap<Packet, NetSocket> packetsToSend;
    private NetServer netServer;

    public INetworkServer(INetworkManager manager) {
        this.packetSerializer = manager.getPacketSerializer();
        this.packetsToSend = new LinkedHashMap<>();
        this.manager = manager;
        this.connectedSockets = new ArrayList<>();
        this.waitingForResponse = new HashMap<>();
        this.recordParser = RecordParser.newDelimited("\n", buffer -> onDataReceive(buffer.toString()));
    }

    public abstract void onClientConnect(NetSocket netSocket);

    public Optional<CloudSocket> getCloudSocketBySocketAddress(SocketAddress address) {
        return this.connectedSockets.stream().filter(netSocket -> netSocket.netSocket().remoteAddress().equals(address)).findFirst();
    }

    public Future<?> startServer(int port) {
        int p = port;
        try {
            p = PortChecker.getFreePort(1000, 9999, port);
        } catch (Exception e) {
            Cloud.getModule().getCloudLogger().error(e);
        }

        netServer = Cloud.getModule().getComponent(Vertx.class).createNetServer();

        netServer.connectHandler(this::onSocketConnect);
        netServer.exceptionHandler(throwable -> {
            Cloud.getModule().getCloudLogger().error("[Network - Server] NetServer has thrown an exception.");
            Cloud.getModule().getCloudLogger().error(throwable);
        });
        int finalP = p;
        int finalP1 = p;
        return netServer.listen(p).onFailure(throwable -> {
            Cloud.getModule().getCloudLogger().warn("[Network - Server] NetServer could not start listening on port " + finalP);
            Cloud.getModule().getCloudLogger().error(throwable);
        }).onSuccess(netServer -> {
            Cloud.getModule().getCloudLogger().info("[Network - Server] NetServer is listening on port " + finalP1);
            this.packetsToSend.forEach((packet, netSocket) -> {
                netSocket.write(this.packetSerializer.serialize(packet) + "\n");
                this.packetsToSend.remove(packet, netSocket);
            });
        });
    }

    private void onSocketConnect(NetSocket socket) {
        if (manager.getTrustedAddresses().stream().noneMatch(inetAddress -> inetAddress.equals(socket.localAddress().hostAddress())))
            return;
        this.connectedSockets.add(new CloudSocket(socket));
        socket.handler(this.recordParser);
        socket.exceptionHandler(throwable -> {
            Cloud.getModule().getCloudLogger().error("[Network - Server] NetSocket has thrown an exception.");
            Cloud.getModule().getCloudLogger().error(throwable);
        });
        socket.closeHandler(unused -> {
            this.connectedSockets.removeIf(cloudSocket -> cloudSocket.netSocket().equals(socket));
            Cloud.getModule().getCloudLogger().info("[Network - Server] NetSocket closed! (" + socket.remoteAddress() + ")");
        });
        socket.exceptionHandler(throwable -> Cloud.getModule().getCloudLogger().error(throwable));
        onClientConnect(socket);
    }

    @SafeVarargs
    public final void sendPacket(Packet packet, NetSocket netSocket, Consumer<Packet>... onResponse) {
        if (PacketType.get(packet.getType_id(), packet.getType_section()).needResponse()) {
            waitingForResponse.put(packet.getRequestID(), onResponse);
        }
        writePacket(packet, netSocket);
    }

    private void writePacket(Packet packet, NetSocket netSocket) {
        if (getNetServer().isEmpty()) {
            this.packetsToSend.put(packet, netSocket);
            return;
        } else netSocket.write(this.packetSerializer.serialize(packet) + "\n");
        Cloud.getModule().getCloudLogger().debug("[Network - Server] Packet sent -> " + packet.toString());
    }

    public Optional<NetServer> getNetServer() {
        return Optional.ofNullable(this.netServer);
    }

    public List<CloudSocket> getConnectedSockets() {
        return connectedSockets;
    }

    public List<CloudSocket> getCloudSockets(ModuleType type) {
        return this.connectedSockets.stream().filter(cloudSocket -> cloudSocket.type().equals(type)).collect(Collectors.toList());
    }

    public void broadcastPacket(Packet packet, CloudSocket... cloudSockets) { //send by the server to send a packet to all clients except those in the array
        getConnectedSockets().forEach(cloudSocket -> {
            if (Arrays.stream(cloudSockets).toList().contains(cloudSocket)) return;
            sendPacket(packet, cloudSocket.netSocket());
        });
    }

    @SafeVarargs
    public final void broadcastPacket(Packet packet, ModuleType moduleType, Consumer<Packet>... onResponse) { //send by the server to send a packet to all clients except those in the array
        getCloudSockets(moduleType).forEach(cloudSocket -> {
            sendPacket(packet, cloudSocket.netSocket(), onResponse);
        });
    }

    public void onDataReceive(String data) {
        Packet packet = this.packetSerializer.deserialize(data);
        if (waitingForResponse.containsKey(packet.getRequestID())) {
            Arrays.stream(waitingForResponse.get(packet.getRequestID())).forEach(packetConsumer -> packetConsumer.accept(packet));
        }
        Cloud.getModule().getCloudLogger().debug("[Network - Server] Packet received -> " + packet + " Delay: " + Duration.between(packet.getCreationTime().getInstant(), Instant.now()).toMillis() + "ms");
        manager.onServerPacket(packet);
        if (packet.getPacketType().needResponse()) return; //wenn packet = status packet return
        broadcastPacket(packet);
    }


    public void stop() {
        if (getNetServer().isPresent()) {
            getNetServer().get().close().onFailure(throwable -> Cloud.getModule().getCloudLogger().error(throwable)).onSuccess(unused -> Cloud.getModule().getCloudLogger().info("[Network - Server] Closed!"));
        }
    }
}
