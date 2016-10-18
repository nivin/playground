package com.gigaspaces.admin.deploy.plan;

import java.util.HashMap;
import java.util.Map;

public class MachineCapabilities implements Cloneable {
    private final Map<String, Object> capabilities = new HashMap<String, Object>();

    public MachineCapabilities set(String key, Object value) {
        capabilities.put(key, value);
        return this;
    }

    public Object get(String key) {
        return capabilities.get(key);
    }

    @Override
    public MachineCapabilities clone() {
        MachineCapabilities copy = new MachineCapabilities();
        for (Map.Entry<String, Object> entry : this.capabilities.entrySet()) {
            copy.capabilities.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }
}
