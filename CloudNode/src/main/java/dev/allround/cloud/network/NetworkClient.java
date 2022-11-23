package dev.allround.cloud.network;

import com.google.gson.Gson;
import dev.allround.cloud.Cloud;
import dev.allround.cloud.ModuleInfo;
import dev.allround.cloud.ModuleType;
import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.util.NodeProperties;
import io.vertx.core.net.NetSocket;

import java.util.function.Consumer;

public class NetworkClient extends INetworkClient {
    public NetworkClient(INetworkManager manager) {
        super(manager);
    }

    @Override
    public void onConnectionSuccess(NetSocket netSocket) {
        sendPacket(new Packet(PacketType.SOCKET_AUTH, ModuleType.NODE.name(), String.valueOf(netSocket.localAddress().port()), netSocket.localAddress().host()), packet -> {
            Cloud.getModule().getCloudLogger().debug("[Network - Client] Socket auth done!");
            sendPacket(new Packet(PacketType.NODE_CONNECT, new Gson().toJson(Cloud.getModule().getModuleInfo())));
        });
        if (Cloud.getModule().getComponent(NodeProperties.class).isMainNode()) return; //die daten requests muss der main node nicht machen da er als erster startet und somit alle daten hat
        sendApiRequest(PacketType.REQUEST_NODE_INFO, new String[0], response -> {
            for (String s : response.getData()) {
                ModuleInfo moduleInfo = new Gson().fromJson(s, ModuleInfo.class);
                ModuleWrapper.getInstance().registerModule(moduleInfo);
            }
        });
    }

    @Override
    public void stop() {
        sendPacket(new Packet(PacketType.NODE_DISCONNECT, new Gson().toJson(ModuleWrapper.getInstance().getThisModule())));
        super.stop();
    }

    @SafeVarargs
    public final void sendApiRequest(PacketType type, String[] data, Consumer<Packet>... onResponse) {
        Packet packet = new Packet(type, data).setSenderHost(getNetSocket().get().localAddress().host()).setSenderPort(getNetSocket().get().localAddress().port());
        sendPacket(packet, onResponse);
    }
}
