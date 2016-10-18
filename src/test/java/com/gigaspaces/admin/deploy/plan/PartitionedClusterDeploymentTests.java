package com.gigaspaces.admin.deploy.plan;

import com.gigaspaces.admin.deploy.plan.impl.DefaultApplicationDeployPlanGenerator;
import org.junit.Assert;
import org.junit.Test;

public class PartitionedClusterDeploymentTests {
    @Test
    public void singlePartition() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")
                            .clusterInfo("schema", "partitioned")
                            .clusterInfo("partitions", 1)))
                .machines(1, new MachineType("medium"));

        ApplicationDeployPlan deployPlan = new DefaultApplicationDeployPlanGenerator().generate(request);

        Assert.assertEquals(1, deployPlan.getMachines().size());
        MachineDeployPlan machine0 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine0 .getMachineId());
        Assert.assertEquals(1, machine0.getInstances().size());
        assertPrimary(machine0.getInstances().get(0), 0);
    }

    @Test
    public void singlePartitionWithBackup() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")
                            .clusterInfo("schema", "partitioned")
                            .clusterInfo("partitions", 1)
                            .clusterInfo("backupsPerPartition", 1)))
                .machines(2, new MachineType("medium"));

        ApplicationDeployPlan deployPlan = new DefaultApplicationDeployPlanGenerator().generate(request);

        Assert.assertEquals(2, deployPlan.getMachines().size());

        MachineDeployPlan machine0 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine0.getMachineId());
        Assert.assertEquals(1, machine0.getInstances().size());
        assertPrimary(machine0.getInstances().get(0), 0);

        MachineDeployPlan machine1 = deployPlan.getMachines().get(1);
        Assert.assertEquals(1, machine1.getMachineId());
        Assert.assertEquals(1, machine1.getInstances().size());
        assertBackup(machine1.getInstances().get(0), 0);
    }

    @Test
    public void singlePartitionWithBackupSameMachine() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")
                            .clusterInfo("schema", "partitioned")
                            .clusterInfo("partitions", 1)
                            .clusterInfo("backupsPerPartition", 1)))
                .machines(1, new MachineType("medium"));

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail();
        } catch (IllegalStateException e) {
            System.out.println("Intercepted expected exception - " + e.getMessage());
        }

    }

    @Test
    public void twoPartitionsWithBackup() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")
                            .clusterInfo("schema", "partitioned")
                            .clusterInfo("partitions", 2)
                            .clusterInfo("backupsPerPartition", 1)))
                .machines(2, new MachineType("medium"));

        ApplicationDeployPlan deployPlan = new DefaultApplicationDeployPlanGenerator().generate(request);

        Assert.assertEquals(2, deployPlan.getMachines().size());

        MachineDeployPlan machine0 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine0.getMachineId());
        Assert.assertEquals(2, machine0.getInstances().size());
        assertPrimary(machine0.getInstances().get(0), 0);
        assertBackup(machine0.getInstances().get(1), 1);

        MachineDeployPlan machine1 = deployPlan.getMachines().get(1);
        Assert.assertEquals(1, machine1.getMachineId());
        Assert.assertEquals(2, machine1.getInstances().size());
        assertPrimary(machine1.getInstances().get(0), 1);
        assertBackup(machine1.getInstances().get(1), 0);
    }

    private static void assertInstance(InstanceDeployPlan instance, int partitionId, boolean primary) {
        Assert.assertEquals("foo", instance.getModuleName());
        Assert.assertEquals(partitionId, instance.getTag("partitionId"));
        Assert.assertEquals(primary, instance.getTag("primary"));
    }

    private static void assertPrimary(InstanceDeployPlan instance, int partitionId) {
        assertInstance(instance, partitionId, true);
    }

    private static void assertBackup(InstanceDeployPlan instance, int partitionId) {
        assertInstance(instance, partitionId, false);
    }

}
