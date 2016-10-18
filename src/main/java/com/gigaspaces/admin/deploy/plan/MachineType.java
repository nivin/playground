package com.gigaspaces.admin.deploy.plan;

/**
 * Created by niv on 10/17/2016.
 */
public class MachineType {
    private final String name;
    private final MachineCapabilities capabilities = new MachineCapabilities();

    public MachineType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MachineCapabilities getCapabilities() {
        return capabilities;
    }

    public MachineType machineCapability(String key, Object value) {
        capabilities.set(key, value);
        return this;
    }
}
