package com.gigaspaces.admin.deploy.plan.impl;

import com.gigaspaces.admin.deploy.plan.InstanceDeployPlan;
import com.gigaspaces.admin.deploy.plan.MachineDeployPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MachineDeployPlanImpl extends MachineDeployPlan {
    private final List<InstanceDeployPlan> instances = new ArrayList<InstanceDeployPlan>();
    private final List<InstanceDeployPlan> instancesReadOnly = Collections.unmodifiableList(instances);

    public MachineDeployPlanImpl(int machineId) {
        super(machineId);
    }

    @Override
    public List<InstanceDeployPlan> getInstances() {
        return instancesReadOnly;
    }

    public void add(InstanceDeployPlan instanceDeployPlan) {
        instances.add(instanceDeployPlan);
    }
}
