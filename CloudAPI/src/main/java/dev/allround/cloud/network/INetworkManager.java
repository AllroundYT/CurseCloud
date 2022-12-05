package dev.allround.cloud.network;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.intern.SocketAuthPacketListener;
import dev.allround.cloud.util.Initializeable;
import dev.allround.cloud.util.Stopable;
import io.vertx.core.Vertx;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Consumer;

public abstract class INetworkManager implements Initializeable {
    private final Set<PacketListener> clientListeners;
    private final Set<PacketListener> serverPacketListeners;
    private final Set<String> trustedAddresses;
    private final PacketSerializer packetSerializer;

    public INetworkManager() {
        Cloud.getModule().registerComponent(Vertx.vertx());
        Cloud.getModule().getComponent(Vertx.class).exceptionHandler(throwable -> Cloud.getModule().getCloudLogger().error(throwable));
        this.packetSerializer = new PacketSerializer();
        this.clientListeners = new HashSet<>();
        this.trustedAddresses = new HashSet<>();
        this.serverPacketListeners = new HashSet<>();

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


    public Set<PacketListener> getServerPacketListeners() {
        return serverPacketListeners;
    }

    public Set<String> getTrustedAddresses() {
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

    public Set<PacketListener> getPacketListeners() {
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
