package com.gigaspaces.admin.deploy.plan;

import java.util.HashMap;
import java.util.Map;

public class ModuleClusterInfo {
    private final Map<String,Object> clusterInfo = new HashMap<String, Object>();

    public ModuleClusterInfo add(String key, Object value) {
        this.clusterInfo.put(key, value);
        return this;
    }

    public String getSchema() {
        return String.valueOf(get("schema"));
    }

    public Object get(String key) {
        return clusterInfo.get(key);
    }

    public Object get(String key, Object defaultValue) {
        return clusterInfo.containsKey(key) ? clusterInfo.get(key) : defaultValue;
    }
}
