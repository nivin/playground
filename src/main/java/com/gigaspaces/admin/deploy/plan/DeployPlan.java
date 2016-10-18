package com.gigaspaces.admin.deploy.plan;

import java.util.ArrayList;
import java.util.List;

public class DeployPlan {
    private final List<MachineDeploymentInfo> machines = new ArrayList<MachineDeploymentInfo>();

    public List<MachineDeploymentInfo> getMachines() {
        return machines;
    }

    public void addInstance(int machineId, InstanceDeploymentInfo instanceDeploymentInfo) {
        if (getMachines().size() <= machineId)
            getMachines().add(new MachineDeploymentInfo(machineId));
        getMachines().get(machineId).getInstances().add(instanceDeploymentInfo);
    }
}
