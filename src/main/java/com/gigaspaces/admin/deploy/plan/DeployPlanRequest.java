package com.gigaspaces.admin.deploy.plan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by niv on 10/16/2016.
 */
public class DeployPlanRequest {
    private final Map<String, Module> modules = new HashMap<String, Module>();
    private final Map<MachineType, Integer> machineTypes = new HashMap<MachineType, Integer>();

    public DeployPlanRequest module(Module module) {
        this.modules.put(module.getName(), module);
        return this;
    }

    public Map<String, Module> getModules() {
        return modules;
    }

    public DeployPlanRequest machines(int count, MachineType machineType) {
        machineTypes.put(machineType, count);
        return this;
    }

    public Map<MachineType, Integer> getMachines() {
        return machineTypes;
    }
}
