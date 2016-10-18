package com.gigaspaces.admin.deploy.plan;

/**
 * Created by niv on 10/17/2016.
 */
public abstract class DeployPlanGenerator {

    public abstract ApplicationDeployPlan generate(DeployPlanRequest request);
}
