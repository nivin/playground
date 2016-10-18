package com.gigaspaces.admin.deploy.plan;

import java.util.List;

public abstract class ApplicationDeployPlan {

    public abstract List<MachineDeployPlan> getMachines();
}
