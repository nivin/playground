package com.gigaspaces.admin.deploy.plan.impl;

import com.gigaspaces.admin.deploy.plan.ApplicationDeployPlan;
import com.gigaspaces.admin.deploy.plan.InstanceDeployPlan;
import com.gigaspaces.admin.deploy.plan.MachineDeployPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationDeployPlanImpl extends ApplicationDeployPlan {

    private final List<MachineDeployPlan> machines = new ArrayList<MachineDeployPlan>();
    private final List<MachineDeployPlan> machinesReadOnly = Collections.unmodifiableList(machines);

    @Override
    public List<MachineDeployPlan> getMachines() {
        return machinesReadOnly;
    }

    public void addInstance(int machineId, InstanceDeployPlan instanceDeployPlan) {
        if (machines.size() <= machineId)
            machines.add(new MachineDeployPlanImpl(machineId));
        ((MachineDeployPlanImpl) machines.get(machineId)).add(instanceDeployPlan);
    }
}
