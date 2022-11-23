package dev.allround.cloud.player;

import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.IService;

import java.util.ArrayList;
import java.util.List;

public interface ICloudPlayer extends ICommandSender {
    String getService();

    String getProxy();
    boolean isOnline();

    void kick(String reason);

    void send(IService iService);

    List<String> getData();

    default Packet createPlayerInfoUpdatePacket(){
        List<String> data = new ArrayList<>(List.of(
                getName(),
                String.valueOf(getUuid()),
                getService(),
                String.valueOf(isOnline()))
        );
        data.addAll(getData());

        return new Packet(PacketType.PLAYER_INFO_UPDATE,data.toArray(new String[0]));
    }

}
