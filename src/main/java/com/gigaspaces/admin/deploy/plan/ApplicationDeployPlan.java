package com.gigaspaces.admin.deploy.plan;

import java.util.List;

public abstract class ApplicationDeployPlan {

    public abstract List<MachineDeployPlan> getMachines();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MachineDeployPlan machine : getMachines()) {
            sb.append(machine);
        }

        return sb.toString();
    }
}
