package com.gigaspaces.admin.deploy.plan;

public class Module {
    private final String name;
    private final ModuleClusterInfo clusterInfo = new ModuleClusterInfo();
    private final InstanceRequirements instanceRequirements = new InstanceRequirements();

    public Module(String name) {
        this.name = name;
    }

    public Module clusterInfo(String key, Object value) {
        this.clusterInfo.add(key, value);
        return this;
    }

    public Module instanceRequirement(String key, Object value) {
        instanceRequirements.add(key, value);
        return this;
    }

    public String getName() {
        return name;
    }

    public ModuleClusterInfo getClusterInfo() {
        return clusterInfo;
    }

    public InstanceRequirements getInstanceRequirements() {
        return instanceRequirements;
    }
}
