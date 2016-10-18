package com.gigaspaces.admin.deploy.plan;

import java.util.ArrayList;
import java.util.List;

public class MachineDeploymentInfo {
    private final int machineId;
    private final List<InstanceDeploymentInfo> instances = new ArrayList<InstanceDeploymentInfo>();

    public MachineDeploymentInfo(int machineId) {
        this.machineId = machineId;
    }

    public int getMachineId() {
        return machineId;
    }

    public List<InstanceDeploymentInfo> getInstances() {
        return instances;
    }
}
