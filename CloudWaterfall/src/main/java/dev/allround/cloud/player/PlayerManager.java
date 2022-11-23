package dev.allround.cloud.player;

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
}