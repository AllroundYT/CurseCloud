package dev.allround.cloud;

import org.jetbrains.annotations.NotNull;

public class Cloud {
    public static String VERSION = "0.1-Development";
    private static CloudModule module;
    private static ModuleWrapper wrapper;

    public static CloudModule getModule() {
        return module;
    }

    public static void setModule(@NotNull CloudModule module) {
        Cloud.module = module;
        Cloud.wrapper.setThisModulesInfo(module.getModuleInfo());
    }

    public static ModuleWrapper getWrapper() {
        return wrapper;
    }

    public static void setWrapper(ModuleWrapper wrapper) {
        Cloud.wrapper = wrapper;
    }
}
