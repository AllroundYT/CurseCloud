package dev.allround.cloud.player;

import com.google.gson.Gson;
import dev.allround.cloud.command.ICommandSender;
import dev.allround.cloud.network.Packet;
import dev.allround.cloud.network.PacketType;
import dev.allround.cloud.service.IService;

import java.util.List;

public interface ICloudPlayer extends ICommandSender {
    String getService();

    String getProxy();

    boolean isOnline();

    boolean isOperator();

    void clonePlayerInfo(ICloudPlayer cloudPlayer);

    void kick(String reason);

    void send(IService iService);

    List<String> getData();

    default Packet createPlayerInfoUpdatePacket() {
        return new Packet(
                PacketType.PLAYER_INFO_UPDATE,
                new Gson().toJson(
                        new CloudPlayerInfoSnapshot(
                                getUuid(),
                                getName(),
                                getProxy(),
                                getService(),
                                isOperator(),
                                isOnline(),
                                getData().toArray(new String[0])
                        ))
        );
        /*List<String> data = new ArrayList<>(List.of(
                getName(),
                String.valueOf(getUuid()),
                getService(),
                getProxy(),
                String.valueOf(isOnline()),
                String.valueOf(isOperator()))
        );
        data.addAll(getData());

        return new Packet(PacketType.PLAYER_INFO_UPDATE,data.toArray(new String[0]));

         */
    }

}
