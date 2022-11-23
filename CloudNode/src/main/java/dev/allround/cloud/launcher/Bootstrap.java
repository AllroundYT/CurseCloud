package dev.allround.cloud.launcher;

import dev.allround.cloud.CloudNode;
import dev.allround.cloud.ModuleWrapper;
import dev.allround.cloud.util.Startable;

import java.util.UUID;

public class Bootstrap implements Startable {

    @Override
    public void start() {
        ModuleWrapper.init();
        CloudNode cloudNode = new CloudNode("Node-"+ UUID.randomUUID().toString().split("-")[0],"0.1-Dev");
        cloudNode.init();
        cloudNode.start();
        Runtime.getRuntime().addShutdownHook(new Thread(cloudNode::stop));
    }
}
