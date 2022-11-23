package dev.allround.cloud;

public class Cloud {
    private static CloudModule module;
    private static ModuleWrapper wrapper;

    public static CloudModule getModule() {
        return module;
    }

    public static void setModule(CloudModule module) {
        Cloud.module = module;
    }

    public static void setWrapper(ModuleWrapper wrapper) {
        Cloud.wrapper = wrapper;
    }

    public static ModuleWrapper getWrapper() {
        return wrapper;
    }
}
