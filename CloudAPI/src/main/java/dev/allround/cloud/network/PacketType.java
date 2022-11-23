package dev.allround.cloud.network;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PacketType {
    //STATUS / AUTH
    SERVER_STATUS_KEEP_ALIVE(0, 0, true), //sent by server in a fixed amount of time
    CLIENT_STATUS_KEEP_ALIVE(1, 0, false), //sent by the client as a response to the servers keep alive packet
    SOCKET_AUTH(2, 0, true),
    SOCKET_AUTH_SUCCESS(3, 0, false),
    //EVENT
    EVENT(0, 2, false), //sent by client to server on event
    //PLAYER
    PLAYER_CONNECT(0, 3, false),
    PLAYER_DISCONNECT(1, 3, false),
    PLAYER_SWITCH_SERVICE(2, 3, false),
    PLAYER_INFO_UPDATE(4, 3, false),
    PLAYER_KICKED(5,3,false),
    //PROXY
    PROXY_START(0,4,false),
    PROXY_STOP(1,4,false),
    PROXY_CONNECT(2, 4, false),
    PROXY_DISCONNECT(3, 4, false),
    PROXY_INFO_UPDATE(4, 4, false),
    //SERVICE
    SERVICE_CONNECT(0, 5, false),
    SERVICE_DISCONNECT(1, 5, false),
    SERVICE_START(2, 5, false),
    SERVICE_STOP(3, 5, false),
    SERVICE_INFO_UPDATE(4, 5, false),
    //SERVICE GROUP
    GROUP_CREATE(0, 6, false),
    GROUP_DELETE(1, 6, false),
    GROUP_INFO_UPDATE(2, 6, false),
    //CLUSTER
    NODE_CONNECT(0, 7, false),
    NODE_DISCONNECT(1, 7, false),
    NODE_INFO_UPDATE(2, 7, false),
    //API
    REQUEST_PLAYER_INFO(0, 8, true),
    REQUEST_SERVICE_INFO(1, 8, true),
    REQUEST_NODE_INFO(2, 8, true),
    REQUEST_GROUP_INFO(3, 8, true),
    REQUEST_PROXY_INFO(4, 8, true),
    REQUEST_PROXY_GROUP_INFO(5, 8, true), //returnt liste mit allen connected netsockets und infos über deren systeme
    API_SEND_MSG_TO_PLAYER(6, 8, false),
    API_KICK_PLAYER(7, 8, true),
    API_SEND_PLAYER(8, 8, true),
    API_START_SERVICES(9, 8, true),
    API_STOP_SERVICE(10, 8, true),
    API_RESTART_SERVICE_GROUP(11, 8, true),
    API_STOP_SERVICE_GROUP(12, 8, true),
    API_START_SERVICE_GROUP(13, 8, true),
    API_START_PROXIES(14, 8, true),
    API_RESTART_PROXY(15, 8, true),
    API_STOP_PROXY(16, 8, true),
    //API RESULT
    RESPONSE_PLAYER_INFO(0, 9, false),
    RESPONSE_SERVICE_INFO(1, 9, false),
    RESPONSE_NODE_INFO(2, 9, false),
    RESPONSE_GROUP_INFO(3, 9, false),
    RESPONSE_PROXY_INFO(4, 9, false),
    RESPONSE_PROXY_GROUP_INFO(5, 9, false),
    API_RESULT_KICK_PLAYER(6, 9, false),
    API_RESULT_SEND_PLAYER(7, 9, false),
    API_RESULT_START_SERVICES(8, 9, false),
    API_RESULT_STOP_SERVICE(9, 9, false),
    API_RESULT_RESTART_SERVICE_GROUP(10, 9, false),
    API_RESULT_STOP_SERVICE_GROUP(11, 9, false),
    API_RESULT_START_SERVICE_GROUP(12, 9, false),
    API_RESULT_START_PROXIES(13, 9, false),
    API_RESULT_RESTART_PROXY(14, 9, false),
    API_RESULT_STOP_PROXY(15, 9, false),

    //Extension packet type
    CUSTOM_INFO(0,10,false),
    CUSTOM_REQUEST(1,10,true),
    CUSTOM_RESPONSE(2,10,false)
    ;
    private final int id;
    private final int section;
    private final boolean needResponse;

    PacketType(int id, int section, boolean needResponse) {
        this.id = id;
        this.section = section;
        this.needResponse = needResponse;
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
        return needResponse;
    }

    public int getId() {
        return id;
    }

    public int getSection() {
        return section;
    }
}
