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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Instance #" + getInstanceId());
        if (!tags.isEmpty()) {
            sb.append(" [");
            boolean isFirst = true;
            for (Map.Entry<String, Object> tag : tags.entrySet()) {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append(", ");
                sb.append(tag.getKey()).append("=").append(tag.getValue());
            }

            sb.append("]");
        }
        return sb.toString();
    }
}
