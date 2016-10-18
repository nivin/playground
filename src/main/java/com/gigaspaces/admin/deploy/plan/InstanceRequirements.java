package com.gigaspaces.admin.deploy.plan;

import java.util.HashMap;
import java.util.Map;

public class InstanceRequirements {
    private final Map<String, Object> requirements = new HashMap<String, Object>();

    public InstanceRequirements add(String key, Object value) {
        requirements.put(key, value);
        return this;
    }

    public Map<String, Object> getRequirements() {
        return requirements;
    }
}
