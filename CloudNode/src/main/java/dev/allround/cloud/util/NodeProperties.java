package dev.allround.cloud.util;

import dev.allround.cloud.Cloud;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.UUID;

public class NodeProperties extends Properties {

    private final Path path;

    public NodeProperties(Path path) {
        this.path = path;
        FileUtils.getFile(path.toAbsolutePath());
    }

    public void addDefault(String key,String value){
        if (getProperty(key) == null) setProperty(key,value);
    }

    public synchronized NodeProperties load(){
        try {
            super.load(Files.newInputStream(path));
            addDefault("node.name", "Node-"+ UUID.randomUUID().toString().split("-")[0]);
            addDefault("node.version", Cloud.getWrapper().getThisModule().version());
            addDefault("node.main", "true");
            addDefault("network.server.port", "1099");
            addDefault("network.server.host", "127.0.0.1");
            addDefault("log.debug", "false");
        }catch (Exception e){
            Cloud.getModule().getCloudLogger().error(e);
        }
        return this;
    }

    public synchronized NodeProperties save() {
        try {
            super.store(Files.newOutputStream(path), "NodeProperties for AllroundCloud Node");
        }catch (Exception e){
            Cloud.getModule().getCloudLogger().error(e);
        }
        return this;
    }

    public String getNodeName(){
        return getProperty("node.name");
    }

    public String getNodeVersion(){
        return getProperty("node.version");
    }

    public boolean isMainNode(){
        return Boolean.parseBoolean(getProperty("node.main"));
    }

    public int getNetworkServerPort(){
        return Integer.parseInt(getProperty("network.server.port"));
    }

    public String getNetworkServerHost(){
        return getProperty("network.server.host");
    }
    public boolean isDebugModeEnabled(){
        return Boolean.parseBoolean(getProperty("log.debug"));
    }
}
