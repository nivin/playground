package com.gigaspaces.admin.deploy.plan;

import org.junit.Assert;
import org.junit.Test;

public class StatelessClusterDeploymentTests {
    @Test
    public void singleInstance() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "stateless")
                        .clusterInfo("instances", 1))
                .machines(1, new MachineType("medium"));

        ApplicationDeployPlan deployPlan = new DefaultDeployPlanGenerator().generate(request);
        Assert.assertEquals(1, deployPlan.getMachines().size());
        MachineDeployPlan machine1 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine1.getMachineId());
        Assert.assertEquals(1, machine1.getInstances().size());
        InstanceDeployPlan instance1 = machine1.getInstances().get(0);
        Assert.assertEquals("foo", instance1.getModuleName());
        Assert.assertEquals(0, instance1.getInstanceId());
    }

    @Test
    public void twoInstancesSameMachine() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "stateless")
                        .clusterInfo("instances", 2)
                        .instanceRequirement("memory", 1024))
                .machines(1, new MachineType("medium")
                        .machineCapability("memory", 2048));

        ApplicationDeployPlan deployPlan = new DefaultDeployPlanGenerator().generate(request);
        Assert.assertEquals(1, deployPlan.getMachines().size());

        MachineDeployPlan machine0 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine0.getMachineId());
        Assert.assertEquals(2, machine0.getInstances().size());

        InstanceDeployPlan instance0 = machine0.getInstances().get(0);
        Assert.assertEquals("foo", instance0.getModuleName());
        Assert.assertEquals(0, instance0.getInstanceId());

        InstanceDeployPlan instance1 = machine0.getInstances().get(1);
        Assert.assertEquals("foo", instance1.getModuleName());
        Assert.assertEquals(1, instance1.getInstanceId());
    }

    @Test
    public void twoInstancesSameMachineInsufficientMemory() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "stateless")
                        .clusterInfo("instances", 2)
                        .instanceRequirement("memory", 1024))
                .machines(1, new MachineType("medium")
                        .machineCapability("memory", 1025));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail();
        } catch (IllegalStateException e) {
            System.out.println("Intercepted expected exception - " + e.getMessage());
        }
    }

    @Test
    public void twoInstancesTwoMachines() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "stateless")
                        .clusterInfo("instances", 2))
                .machines(2, new MachineType("medium"));

        ApplicationDeployPlan deployPlan = new DefaultDeployPlanGenerator().generate(request);
        Assert.assertEquals(2, deployPlan.getMachines().size());

        MachineDeployPlan machine0 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine0.getMachineId());
        Assert.assertEquals(1, machine0.getInstances().size());
        InstanceDeployPlan instance0 = machine0.getInstances().get(0);
        Assert.assertEquals("foo", instance0.getModuleName());
        Assert.assertEquals(0, instance0.getInstanceId());

        MachineDeployPlan machine1 = deployPlan.getMachines().get(1);
        Assert.assertEquals(1, machine1.getMachineId());
        Assert.assertEquals(1, machine1.getInstances().size());
        InstanceDeployPlan instance1 = machine1.getInstances().get(0);
        Assert.assertEquals("foo", instance1.getModuleName());
        Assert.assertEquals(1, instance1.getInstanceId());
    }
}
