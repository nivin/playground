package com.gigaspaces.admin.deploy.plan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by niv on 10/17/2016.
 */
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
