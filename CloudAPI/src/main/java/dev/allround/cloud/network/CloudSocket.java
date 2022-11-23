package dev.allround.cloud.network;

import dev.allround.cloud.ModuleType;
import io.vertx.core.net.NetSocket;

public class CloudSocket {
    private final NetSocket netSocket;
    private ModuleType type;

    public CloudSocket(NetSocket netSocket) {
        this.netSocket = netSocket;
        this.type = ModuleType.UNKNOWN;
    }

    public NetSocket netSocket() {
        return netSocket;
    }

    public ModuleType type() {
        return type;
    }

    public CloudSocket setType(ModuleType type) {
        this.type = type;
        return this;
    }
}
