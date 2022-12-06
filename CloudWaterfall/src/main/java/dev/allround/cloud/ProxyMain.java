package dev.allround.cloud;

import dev.allround.cloud.command.CommandManager;
import dev.allround.cloud.log.CloudProxyLogger;
import dev.allround.cloud.network.INetworkClient;
import dev.allround.cloud.network.INetworkManager;
import dev.allround.cloud.network.NetworkClient;
import dev.allround.cloud.network.NetworkManager;
import dev.allround.cloud.player.PlayerManager;
import dev.allround.cloud.service.ServiceManager;
import dev.allround.cloud.servicegroup.ServiceGroupManager;
import dev.allround.cloud.util.Startable;
import dev.allround.cloud.util.Stopable;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class ProxyMain  implements CloudModule {
    private final String name;
    private final String version;
    private final UUID id;
    private final ArrayList<Object> components;

    public ProxyMain() {
        this.components = new ArrayList<>();
        this.name = System.getProperty("cloud.proxy.name");
        this.version = "0.1-Dev";
        this.id = UUID.randomUUID();
    }

    @Override
    public List<Object> getComponents() {
        return components;
    }

    @Override
    public ModuleInfo getModuleInfo() {
        return new ModuleInfo(this.name, this.version, this.id, ModuleType.PROXY);
    }

    @Override
    public void init() {
        Cloud.setModule(this); //don't touch
        getComponents().clear(); //don't touch

        registerComponent(new CloudProxyLogger());
        registerComponent(new ProxyProperties(System.getProperty("cloud.service.id"),System.getProperty("cloud.service.group"),Integer.getInteger("cloud.network.port"),System.getProperty("cloud.network.host")));
        registerComponent(new ModuleInfo(System.getProperty("cloud.service.id"),Cloud.VERSION,UUID.randomUUID(),ModuleType.PROXY));
        registerComponent(Executors.newCachedThreadPool()); //don't touch
        registerComponent(Executors.newScheduledThreadPool(1)); //don't touch

        registerComponent(new NetworkManager());
        registerComponent(new NetworkClient(getComponent(INetworkManager.class)));
        registerComponent(new PlayerManager());
        registerComponent(new ServiceManager());
        registerComponent(new ServiceGroupManager());
        registerComponent(new CommandManager());
    }

    @Override
    public void start() {
        getCloudLogger().info("CloudProxyPlugin is starting...");
        Instant start = Instant.now();

        getComponents().forEach(o -> {
            if (o instanceof Startable startable) startable.start();
        });

        ProxyProperties proxyProperties = getComponent(ProxyProperties.class);
        getComponent(INetworkClient.class).startClient(new InetSocketAddress(proxyProperties.networkServerHost(), proxyProperties.networkServerPort()));

        getCloudLogger().info("CloudProxyPlugin started successfully. (" + Duration.between(start, Instant.now()).toMillis() + "ms)");
    }

    @Override
    public void stop() {
        getCloudLogger().info("CloudProxyPlugin is stopping...");
        Instant start = Instant.now();

        getComponents().forEach(o -> {
            if (o instanceof Stopable stopable) stopable.stop();
        });

        getCloudLogger().info("CloudProxyPlugin stopped successfully. (" + Duration.between(start, Instant.now()).toMillis() + "ms)");
    }
}
