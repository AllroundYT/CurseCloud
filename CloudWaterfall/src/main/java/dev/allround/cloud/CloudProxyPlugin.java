package dev.allround.cloud;

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
        proxyMain.start();
    }

    @Override
    public void onDisable() {
        proxyMain.stop();
    }
}
