package com.gigaspaces.admin.deploy.plan;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDeployPlanRequest {
    private ApplicationDetails applicationDetails;
    private final Map<MachineType, Integer> machineTypes = new HashMap<MachineType, Integer>();


    public ApplicationDeployPlanRequest application(ApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
        return this;
    }

    public ApplicationDetails getApplicationDetails() {
        return applicationDetails;
    }

    public ApplicationDeployPlanRequest machines(int count, MachineType machineType) {
        machineTypes.put(machineType, count);
        return this;
    }

    public Map<MachineType, Integer> getMachines() {
        return machineTypes;
    }
}
