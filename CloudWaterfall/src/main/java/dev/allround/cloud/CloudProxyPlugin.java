package dev.allround.cloud;

import dev.allround.cloud.command.CloudCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class CloudProxyPlugin extends Plugin {
    private final ProxyMain proxyMain;

    public CloudProxyPlugin() {
        this.proxyMain = new ProxyMain();
    }

    @Override
    public void onLoad() {
        ModuleWrapper.init();
        proxyMain.registerComponent(this);
        proxyMain.init();
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new CloudCommand());
        proxyMain.start();
    }

    @Override
    public void onDisable() {
        proxyMain.stop();
    }
}
