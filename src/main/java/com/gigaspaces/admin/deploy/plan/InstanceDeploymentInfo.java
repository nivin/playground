package com.gigaspaces.admin.deploy.plan;

import java.util.HashMap;
import java.util.Map;

public class InstanceDeploymentInfo {
    private final int instanceId;
    private final String moduleName;
    private final Map<String, Object> tags = new HashMap<String, Object>();

    public InstanceDeploymentInfo(int instanceId, String moduleName) {
        this.instanceId = instanceId;
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public InstanceDeploymentInfo tag(String key, Object value) {
        tags.put(key, value);
        return this;
    }

    public Object getTag(String key) {
        return tags.get(key);
    }
}
