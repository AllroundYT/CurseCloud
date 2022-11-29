package dev.allround.cloud.service;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.player.ICloudPlayer;
import dev.allround.cloud.player.IPlayerManager;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;
import io.vertx.core.net.SocketAddress;

import java.util.List;
import java.util.stream.Collectors;

public interface IService extends Startable, Stopable { //TODO: Service muss neu gemacht werden damit er als server oder proxy genutzt werden kann
    SocketAddress getSocketAddress();

    String[] getMotd();

    void setMotd(String[] motd);

    ServiceType getType();

    ServiceVersion getServiceVersion();

    Process getProcess();

    void setProcess(Process process);

    String getStartArgs();


    int getMaxRam();

    default boolean isOnline() {
        return !List.of("BLOCKED", "STOPPING", "OFFLINE").contains(getStatus().toUpperCase());
    }

    default boolean isStarting() {
        return List.of("READY", "CONNECTED", "CREATED").contains(getStatus().toUpperCase());
    }

    default boolean isStopping() {
        return List.of("BLOCKED", "STOPPING").contains(getStatus().toUpperCase());
    }

    /**
     * Bedeutungen:
     * READY → kann gestartet werden
     * CONNECTED → ist verbunden mit der Cloud kann aber noch nicht betreten werden
     * RUNNING → ist fertig gestartet und kann betreten werden
     * MAINTENANCE → kann nur mit der berechtigung cloud.service.join.maintenance betreten werden
     * PRIVATE → kann nur mit der berechtigung cloud.service.join.private betreten werden
     * INGAME → kann nur mit der berechtigung cloud.service.join.ingame betreten werden (sollte für Minigames genutzt werden)
     * BLOCKED → kann nicht mehr betreten werden, bereitet stop vor (wenn man den Server stoppen möchte, muss man diesen Status setzten)
     * STOPPING → server ist am Stoppen und es kann nicht gejoint werden
     * OFFLINE → ist gestoppt und wird in Kürze gelöscht
     * <p>
     * Der Status kann auch ein beliebiger sein bei genannten Staten wird jedoch die aktion in Kraft gesetzt.
     * Manche Staten (READY, CONNECTED, STOPPING, OFFLINE) können nur von der Cloud gesetzt werden (werden automatisch gesetzt)
     */
    String getStatus();


    /**
     * Bedeutungen:
     * READY → kann gestartet werden
     * CONNECTED → ist verbunden mit der Cloud kann aber noch nicht betreten werden
     * RUNNING → ist fertig gestartet und kann betreten werden
     * MAINTENANCE → kann nur mit der berechtigung cloud.service.join.maintenance betreten werden
     * PRIVATE → kann nur mit der berechtigung cloud.service.join.private betreten werden
     * INGAME → kann nur mit der berechtigung cloud.service.join.ingame betreten werden (sollte für Minigames genutzt werden)
     * BLOCKED → kann nicht mehr betreten werden, bereitet stop vor (wenn man den Server stoppen möchte, muss man diesen Status setzten)
     * STOPPING → server ist am Stoppen und es kann nicht gejoint werden
     * OFFLINE → ist gestoppt und wird in Kürze gelöscht
     * <p>
     * Der Status kann auch ein beliebiger sein bei genannten Staten wird jedoch die aktion in Kraft gesetzt.
     * Manche Staten (READY, CONNECTED, STOPPING, OFFLINE) können nur von der Cloud gesetzt werden (werden automatisch gesetzt)
     */
    void setStatus(String status);

    String getServiceID();

    int getMaxPlayers();

    void setMaxPlayers(int i);

    default List<ICloudPlayer> getPlayers() {
        return Cloud.getModule().getComponent(IPlayerManager.class)
                .getCloudPlayers()
                .stream()
                .filter(iCloudPlayer -> iCloudPlayer.getService().equals(getServiceID()))
                .collect(Collectors.toList());
    }

    String getServiceGroup();

    String getJavaParams();

    boolean copyTemplate(boolean printWarnMsg);

    //double getTps();

    String getNode();

    default Packet createServiceInfoUpdatePacket() {
        return new Packet(
                PacketType.SERVICE_INFO_UPDATE,
                getServiceID(),
                getStatus(),
                getServiceGroup(),
                getNode(),
                getType().name(),
                getServiceVersion().name(),
                getJavaParams(),
                getStartArgs(),
                String.valueOf(getSocketAddress().port()),
                getSocketAddress().host(),
                String.valueOf(getMaxPlayers()),
                getMotd()[0],
                getMotd()[1]
        );
    }
}
