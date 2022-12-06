package dev.allround.cloud.player;

import dev.allround.cloud.Cloud;
import dev.allround.cloud.network.INetworkClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerManager implements IPlayerManager {
    private final List<ICloudPlayer> cloudPlayers;

    public PlayerManager() {
        this.cloudPlayers = new ArrayList<>();
    }

    @Override
    public Optional<ICloudPlayer> getCloudPlayer(UUID uuid) {
        return cloudPlayers.stream().filter(iCloudPlayer -> iCloudPlayer.getUuid().equals(uuid)).findFirst();
    }

    @Override
    public List<ICloudPlayer> getCloudPlayers() {
        return cloudPlayers;
    }

    @Override
    public void registerPlayer(ICloudPlayer iCloudPlayer) {
        this.cloudPlayers.add(iCloudPlayer);
        Cloud.getModule().getComponent(INetworkClient.class).sendPacket(iCloudPlayer.createPlayerInfoUpdatePacket());
    }

    @Override
    public void update(ICloudPlayer cloudPlayer){
        if (getCloudPlayer(cloudPlayer.getUuid()).isEmpty()) {
            this.cloudPlayers.add(cloudPlayer);
        } else {
            getCloudPlayer(cloudPlayer.getUuid()).get().clonePlayerInfo(cloudPlayer);
        }
    }

    @Override
    public void registerPlayer(UUID uuid, String name) {
        registerPlayer(new CloudPlayer(uuid,name));
    }
}
