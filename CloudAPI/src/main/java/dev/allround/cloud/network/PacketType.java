package dev.allround.cloud.network;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PacketType {
    //STATUS / AUTH
    /**
     *
     */
    SERVER_STATUS_KEEP_ALIVE(0, 0, true), //sent by server in a fixed amount of time
    /**
     *
     */
    CLIENT_STATUS_KEEP_ALIVE(1, 0, false), //sent by the client as a response to the servers keep alive packet
    /**
     *
     */
    SOCKET_AUTH(2, 0, true),
    /**
     *
     */
    SOCKET_AUTH_SUCCESS(3, 0, false),
    //PLAYER
    /**
     * UUID
     */
    PLAYER_CONNECTED(0, 3, false),
    /**
     * UUID
     */
    PLAYER_DISCONNECTED(1, 3, false),
    /**
     * UUID,SERVICE
     */
    PLAYER_SWITCHED_SERVICE(2, 3, false),
    /**
     * PLAYER INFO SNAPSHOT
     */
    PLAYER_INFO_UPDATED(4, 3, false),
    /**
     * PLAYER UUID
     */
    PLAYER_KICKED(5, 3, false),
    //SERVICE
    /**
     * SERVICE ID
     */
    SERVICE_CONNECTED(0, 5, false),
    /**
     * SERVICE ID
     */
    SERVICE_DISCONNECTED(1, 5, false),
    /**
     * SERVICE ID
     */
    SERVICE_STARTED(2, 5, false),
    /**
     * SERVICE ID
     */
    SERVICE_STOPPED(3, 5, false),
    /**
     * SERVICE INFO SNAPSHOT
     */
    SERVICE_INFO_UPDATED(4, 5, false),
    //SERVICE GROUP

    /**
     * GROUP NAME
     */
    GROUP_DELETED(0, 6, false),
    /**
     * GROUP INFO SNAPSHOT
     */
    GROUP_INFO_UPDATED(1, 6, false),
    //CLUSTER
    /**
     * NODE (MODULE INFO)
     */
    NODE_CONNECTED(0, 7, false),
    /**
     * NODE (MODULE INFO)
     */
    NODE_DISCONNECTED(1, 7, false),
    /**
     * NODE INFO (MODULE INFO)
     */
    NODE_INFO_UPDATED(2, 7, false),
    /**
     *
     */
    CLUSTER_STOPPED(3, 7, false),
    //API
    /**
     *
     */
    REQUEST_PLAYER_INFO(0, 8, true),
    /**
     *
     */
    REQUEST_SERVICE_INFO(1, 8, true),
    /**
     *
     */
    REQUEST_NODE_INFO(2, 8, true),
    /**
     *
     */
    REQUEST_GROUP_INFO(3, 8, true),
    /**
     *
     */
    API_SEND_MSG_TO_PLAYER(6, 8, false), //PLAYER (UUID), MESSAGE (STRING)
    /**
     *
     */
    API_KICK_PLAYER(7, 8, true), //PLAYER (UUID), REASON (STRING)
    /**
     *
     */
    API_SEND_PLAYER(8, 8, true), //PLAYER (UUID), SERVICE ID (STRING)
    /**
     *
     */
    API_START_SERVICE_OF_GROUP(9, 8, true), // SERVICE GROUP (STRING)
    /**
     *
     */
    API_STOP_SERVICE(10, 8, true), // SERVICE ID (STRING)
    /**
     *
     */
    API_UPDATE_SERVICE_GROUP(11, 8, true), //SERVICE GROUP (STRING)
    /**
     *
     */
    API_STOP_SERVICE_GROUP(12, 8, true), // SERVICE GROUP (STRING)

    API_STOP_CLOUD(13, 8, false),

    API_START_SERVICE(14, 8, true),
    //API RESULT
    /**
     *
     */
    RESPONSE_PLAYER_INFO(0, 9, false),
    /**
     *
     */
    RESPONSE_SERVICE_INFO(1, 9, false),
    /**
     *
     */
    RESPONSE_NODE_INFO(2, 9, false),
    /**
     *
     */
    RESPONSE_GROUP_INFO(3, 9, false),
    /**
     *
     */
    API_RESULT_KICK_PLAYER(6, 9, false),
    /**
     *
     */
    API_RESULT_SEND_PLAYER(7, 9, false),
    /**
     * Sent as response for an api request (API_STOP_PROXY)
     */
    API_RESULT_START_SERVICE(8, 9, false),
    /**
     * Sent as response for an api request (API_STOP_PROXY)
     */
    API_RESULT_STOP_SERVICE(9, 9, false),
    /**
     * Sent as response for an api request (API_STOP_PROXY)
     */
    API_RESULT_RESTART_SERVICE_GROUP(10, 9, false),
    /**
     * Sent as response for an api request (API_STOP_PROXY)
     */
    API_RESULT_STOP_SERVICE_GROUP(11, 9, false),
    /**
     * Sent as response for an api request (API_STOP_PROXY)
     */
    API_RESULT_START_SERVICE_GROUP(12, 9, false),

    UNDEFINED_API_RESPONSE(13, 9, false),
    //Extension
    /**
     * Used by Cloud Extensions to send custom information
     * TYPE,DATA...
     */
    CUSTOM_INFO(0, 10, false),
    /**
     * Used by Cloud Extensions to perform a custom API request
     * TYPE,DATA...
     */
    CUSTOM_REQUEST(1, 10, true),
    /**
     * Used by Cloud Extensions as response on a custom API request
     * TYPE,DATA...
     */
    CUSTOM_RESPONSE(2, 10, false),
    /**
     * EXTENSION NAME
     */
    EXTENSION_LOADED(0, 11, false),
    /**
     * EXTENSION NAME
     */
    EXTENSION_STARTED(1, 11, false),
    /**
     * EXTENSION NAME
     */
    EXTENSION_STOPPED(2, 11, false);
    private final int id;
    private final int section;
    private final boolean responsePossible;

    PacketType(int id, int section, boolean responsePossible) {
        this.id = id;
        this.section = section;
        this.responsePossible = responsePossible;
    }

    public static PacketType get(int id, int section) {
        if (Arrays.stream(PacketType.values()).anyMatch(packetType -> packetType.id == id && packetType.section == section)) {
            return Arrays.stream(PacketType.values()).filter(packetType -> packetType.id == id && packetType.section == section).findFirst().get();
        } else return null;
    }

    public static List<PacketType> get(int section) {
        return Arrays.stream(PacketType.values()).filter(packetType -> packetType.section == section).collect(Collectors.toList());
    }

    public boolean needResponse() {
        return responsePossible;
    }

    public int getId() {
        return id;
    }

    public int getSection() {
        return section;
    }
}
