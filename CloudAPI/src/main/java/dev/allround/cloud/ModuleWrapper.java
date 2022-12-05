package dev.allround.cloud;


import java.util.HashSet;
import java.util.Set;

public class ModuleWrapper {
    private static ModuleWrapper instance;
    private final Set<ModuleInfo> moduleInfos;
    private ModuleInfo thisModulesInfo;

    public void setThisModulesInfo(ModuleInfo thisModulesInfo) {
        this.thisModulesInfo = thisModulesInfo;
    }

    public ModuleWrapper() {
        this.moduleInfos = new HashSet<>();
    }

    public static ModuleWrapper getInstance() {
        return instance;
    }

    public static void init() {
        if (ModuleWrapper.instance != null) return;
        ModuleWrapper.instance = new ModuleWrapper();
        Cloud.setWrapper(getInstance());
    }

    public boolean isNotThisModule(String name){
        return !getThisModule().name().equals(name);
    }

    public boolean isThisModule(String name){
        return getThisModule().name().equals(name);
    }

    public Set<ModuleInfo> getModuleInfos() {
        return moduleInfos;
    }

    public void registerModule(ModuleInfo moduleInfo) {
        if (this.moduleInfos.stream().anyMatch(moduleInfo1 -> moduleInfo1.id().equals(moduleInfo.id()))) return;
        this.moduleInfos.add(moduleInfo);
    }

    public ModuleInfo getThisModule() {
        return this.thisModulesInfo;
    }

    public void unregisterModule(ModuleInfo moduleInfo) {
        this.moduleInfos.removeIf(moduleInfo1 -> moduleInfo1.id().equals(moduleInfo.id()));
    }
}
