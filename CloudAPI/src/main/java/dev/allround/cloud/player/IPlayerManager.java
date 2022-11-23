package dev.allround.cloud.player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPlayerManager {
    Optional<ICloudPlayer> getCloudPlayer(UUID uuid);

    List<ICloudPlayer> getCloudPlayers();
}
