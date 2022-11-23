package dev.allround.cloud.network;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.intern.SocketAuthPacketListener;
import dev.allround.cloud.util.Initializeable;
import dev.allround.cloud.util.Stopable;
import io.vertx.core.Vertx;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class INetworkManager implements Stopable, Initializeable {
    private final List<PacketListener> clientListeners;
    private final List<PacketListener> serverPacketListeners;
    private final List<String> trustedAddresses;
    private final PacketSerializer packetSerializer;

    public INetworkManager() {
        Cloud.getModule().registerComponent(Vertx.vertx());
        Cloud.getModule().getComponent(Vertx.class).exceptionHandler(throwable -> Cloud.getModule().getCloudLogger().error(throwable));
        this.packetSerializer = new PacketSerializer();
        this.clientListeners = new ArrayList<>();
        this.trustedAddresses = new ArrayList<>();
        this.serverPacketListeners = new ArrayList<>();

        trustedAddresses.add("127.0.0.1");

        registerServerPacketListener(new SocketAuthPacketListener());
    }

    public static InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void stop() {

    }


    public List<PacketListener> getServerPacketListeners() {
        return serverPacketListeners;
    }

    public List<String> getTrustedAddresses() {
        return trustedAddresses;
    }

    public PacketSerializer getPacketSerializer() {
        return packetSerializer;
    }

    public void registerClientPacketListener(PacketListener... packetListeners) {
        this.clientListeners.addAll(List.of(packetListeners));
    }

    public void registerServerPacketListener(PacketListener... packetListeners) {
        this.serverPacketListeners.addAll(List.of(packetListeners));
    }

    public List<PacketListener> getPacketListeners() {
        return clientListeners;
    }

    public void onPacket(Packet packet) {
        for (PacketListener listener : getPacketListeners()) {

            Class<PacketListener> c = (Class<PacketListener>) listener.getClass();
            Method[] methods = c.getMethods();
            for (Method method : methods) {
                try {
                    PacketHandler eventHandler = method.getAnnotation(PacketHandler.class);
                    if (eventHandler != null && eventHandler.type().equals(PacketType.get(packet.getType_id(), packet.getType_section())) && (method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].isAssignableFrom(packet.getClass())) {

                        method.invoke(listener, packet);
                        Cloud.getModule().getCloudLogger().debug("[Network - Manager] Packet Listener triggered -> PacketType: " + Objects.requireNonNull(PacketType.get(packet.getType_id(), packet.getType_section())).name() + /*" Class: " + c.getName()+*/  " Method: " + method.getName());
                    }
                } catch (Exception e) {
                    Cloud.getModule().getCloudLogger().error(e);
                }
            }

        }
    }

    public void onServerPacket(Packet packet) {
        for (PacketListener listener : getServerPacketListeners()) {

            Class<PacketListener> c = (Class<PacketListener>) listener.getClass();
            Method[] methods = c.getMethods();
            for (Method method : methods) {
                try {
                    PacketHandler eventHandler = method.getAnnotation(PacketHandler.class);
                    if (eventHandler != null && eventHandler.type().equals(PacketType.get(packet.getType_id(), packet.getType_section())) && (method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].isAssignableFrom(packet.getClass())) {

                        method.invoke(listener, packet);
                        Cloud.getModule().getCloudLogger().debug("[Network - Manager] Packet Listener triggered -> PacketType: " + Objects.requireNonNull(PacketType.get(packet.getType_id(), packet.getType_section())).name() + /*" Class: " + c.getName() +*/ " Method: " + method.getName());
                    }
                } catch (Exception e) {
                    Cloud.getModule().getCloudLogger().error(e);
                }
            }

        }
    }

}
