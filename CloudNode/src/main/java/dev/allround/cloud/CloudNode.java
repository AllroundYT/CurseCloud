package dev.allround.cloud;


import dev.allround.cloud.command.CommandManager;
import dev.allround.cloud.command.InputManager;
import dev.allround.cloud.event.EventManager;
import dev.allround.cloud.log.NodeLogger;
import dev.allround.cloud.network.*;
import dev.allround.cloud.service.ServiceManager;
import dev.allround.cloud.setup.SetupManager;
import dev.allround.cloud.servicegroup.ServiceGroupManager;
import dev.allround.cloud.util.*;
import dev.allround.cloud.util.process.ProcessPool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;

public class CloudNode implements CloudModule {
    private String name;
    private final String version;
    private final UUID id;
    private final ArrayList<Object> components;
    private boolean firstStart;

    public boolean isFirstStart() {
        return firstStart;
    }

    public CloudNode(String name, String version) {
        this.components = new ArrayList<>();
        this.name = name;
        this.version = version;
        this.id = UUID.randomUUID();
    }

    public CloudNode setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ArrayList<Object> getComponents() {
        return components;
    }

    @Override
    public ModuleInfo getModuleInfo() {
        return new ModuleInfo(this.name, this.version, this.id, ModuleType.NODE);
    }

    @Override
    public void start() {
        System.out.println(getCloudLogger().getCloudLogo());
        getCloudLogger().info("");
        getCloudLogger().info("Cloudnode is starting...");
        getCloudLogger().info("");
        Instant start = Instant.now();


        ((NodeLogger)getCloudLogger()).setDebugMode(getComponent(NodeProperties.class).isDebugModeEnabled());

        ProgressFuture progressFuture = new ProgressFuture(() -> {
            getCloudLogger().info("");
            getCloudLogger().info("Cloudnode started successfully. (" + Duration.between(start, Instant.now()).toMillis() + "ms)");
            getCloudLogger().info("");
            getCloudLogger().warn("PLEASE KEEP IN MIND THAT YOU HAVE TO SHUTDOWN THIS CLOUD BEFORE KILLING ITS PROCESS!");
            getCloudLogger().info("");
            if (isFirstStart()){
                getCloudLogger().info("This seems to be the first start of this cloud node.");
                getCloudLogger().info("If it shouldn't be the main node please edit the node.properties file and restart the cloud via the restart command.");
                getCloudLogger().info("");
            }

            getComponents().forEach(o -> {
                if (o instanceof Startable startable) startable.start();
            });
        }, getComponent(NodeProperties.class).isMainNode() ? 2 : 1);

        getComponents().forEach(o -> {
            if (o instanceof Initializeable initializeable) initializeable.init();
        });

        ModuleWrapper.getInstance().registerModule(getModuleInfo());

        if (getComponent(NodeProperties.class).isMainNode())
            getComponent(INetworkServer.class).startServer(1099).onSuccess(o -> progressFuture.addProgress(1));
        getComponent(INetworkClient.class).startClient(new InetSocketAddress("127.0.0.1", 1099)).onSuccess(o -> progressFuture.addProgress(1));
    }


    @Override
    public void stop() {
        {
            getCloudLogger().info("");
            getCloudLogger().info("Cloudnode is stopping...");
            getCloudLogger().info("");
        }
        Instant start = Instant.now();

        getComponent(NodeProperties.class).save();

        getComponents().forEach(o -> {
            if (o instanceof Stopable stopable) stopable.stop();
        });

        getCachedThreadPool().shutdownNow();
        getScheduledExecutorService().shutdownNow();

        FileUtils.delete(Path.of("temp").toFile());

        {
            getCloudLogger().info("");
            getCloudLogger().info("Cloudnode stopped successfully. (" + Duration.between(start, Instant.now()).toMillis() + "ms)");
            getCloudLogger().info("");
        }
        System.out.println(getCloudLogger().getCloudLogo());
    }

    public Packet createNodeInfoUpdatePacket(){
        return new Packet(
                PacketType.NODE_INFO_UPDATE,
                getModuleInfo().name(),
                String.valueOf(getModuleInfo().id()),
                getModuleInfo().version()
        );
    }

    @Override
    public void init() {
        Cloud.setModule(this); //don't touch
        getComponents().clear(); //don't touch

        registerComponent(Executors.newCachedThreadPool()); //don't touch
        registerComponent(Executors.newScheduledThreadPool(1)); //don't touch

        registerComponent(new NodeLogger());
        registerComponent(new ProcessPool());

        try {
            FileUtils.setupDirectories(
                    Path.of("logs"),
                    Path.of("templates"),
                    Path.of("templates","proxy"),
                    Path.of("templates","server"),
                    Path.of("security"),
                    Path.of("temp"),
                    Path.of("temp","proxy"),
                    Path.of("temp","server"),
                    Path.of("cache"),
                    Path.of("data")
            );
        } catch (IOException e) {
            getCloudLogger().error(e);
        }

        registerComponent(new NetworkManager());
        registerComponent(new NetworkClient(getComponent(INetworkManager.class)));
        registerComponent(new NetworkServer(getComponent(INetworkManager.class)));
        registerComponent(new EventManager());
        registerComponent(new InputManager());
        registerComponent(new CommandManager());
        registerComponent(new SetupManager());
        registerComponent(new ServiceManager());
        registerComponent(new ServiceGroupManager());

        Path nodePropertiesPath = Path.of("node.properties");
        if (!Files.exists(nodePropertiesPath)) this.firstStart = true;
        registerComponent(new NodeProperties(nodePropertiesPath));
        getComponent(NodeProperties.class).load().save();
        //setName(getComponent(NodeProperties.class).getNodeName()); //TODO: bei wichtigen tests hinzufügen
    }
}
