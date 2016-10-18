package com.gigaspaces.admin.deploy.plan;

import java.util.List;

public abstract class MachineDeployPlan {
    private final int machineId;

    public MachineDeployPlan(int machineId) {
        this.machineId = machineId;
    }

    public int getMachineId() {
        return machineId;
    }

    public abstract List<InstanceDeployPlan> getInstances();
}
