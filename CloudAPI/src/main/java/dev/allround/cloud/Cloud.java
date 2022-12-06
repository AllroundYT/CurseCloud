package dev.allround.cloud;

import org.jetbrains.annotations.NotNull;

public class Cloud {
    private static CloudModule module;
    private static ModuleWrapper wrapper;

    public static String VERSION = "0.1-Development";
    public static CloudModule getModule() {
        return module;
    }

    public static void setModule(@NotNull CloudModule module) {
        Cloud.module = module;
        Cloud.wrapper.setThisModulesInfo(module.getModuleInfo());
    }

    public static void setWrapper(ModuleWrapper wrapper) {
        Cloud.wrapper = wrapper;
    }

    public static ModuleWrapper getWrapper() {
        return wrapper;
    }
}
