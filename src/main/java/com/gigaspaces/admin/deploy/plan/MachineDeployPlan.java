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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Machine #").append(machineId).append(System.getProperty("line.separator"));
        for (InstanceDeployPlan instanceDeployPlan : getInstances()) {
            sb.append("\t").append(instanceDeployPlan).append(System.getProperty("line.separator"));
        }

        return sb.toString();
    }
}
