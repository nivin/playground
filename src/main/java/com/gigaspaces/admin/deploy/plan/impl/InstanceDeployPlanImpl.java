package com.gigaspaces.admin.deploy.plan.impl;

import com.gigaspaces.admin.deploy.plan.InstanceDeployPlan;

import java.util.HashMap;
import java.util.Map;

public class InstanceDeployPlanImpl extends InstanceDeployPlan {

    private final Map<String, Object> tags = new HashMap<String, Object>();

    public InstanceDeployPlanImpl(int instanceId, String moduleName) {
        super(instanceId, moduleName);
    }

    public InstanceDeployPlanImpl tag(String key, Object value) {
        tags.put(key, value);
        return this;
    }

    public Object getTag(String key) {
        return tags.get(key);
    }

}
