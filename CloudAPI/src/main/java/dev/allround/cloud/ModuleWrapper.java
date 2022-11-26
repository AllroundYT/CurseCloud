package dev.allround.cloud;


import java.util.ArrayList;

public class ModuleWrapper {
    private static ModuleWrapper instance;
    private final ArrayList<ModuleInfo> moduleInfos;
    private ModuleInfo thisModulesInfo;

    public void setThisModulesInfo(ModuleInfo thisModulesInfo) {
        this.thisModulesInfo = thisModulesInfo;
    }

    public ModuleWrapper() {
        this.moduleInfos = new ArrayList<>();
    }

    public static ModuleWrapper getInstance() {
        return instance;
    }

    public static void init() {
        if (ModuleWrapper.instance != null) return;
        ModuleWrapper.instance = new ModuleWrapper();
        Cloud.setWrapper(getInstance());
    }

    public boolean isThisModule(String name){
        return getThisModule().name().equals(name);
    }

    public ArrayList<ModuleInfo> getModuleInfos() {
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
