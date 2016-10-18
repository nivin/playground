package com.gigaspaces.admin.deploy.plan;

public abstract class InstanceDeployPlan {
    private final int instanceId;
    private final String moduleName;

    public InstanceDeployPlan(int instanceId, String moduleName) {
        this.instanceId = instanceId;
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public abstract Object getTag(String key);
}
