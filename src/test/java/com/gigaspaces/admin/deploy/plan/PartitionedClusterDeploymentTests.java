package com.gigaspaces.admin.deploy.plan;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by niv on 10/17/2016.
 */
public class PartitionedClusterDeploymentTests {
    @Test
    public void singlePartition() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "partitioned")
                        .clusterInfo("partitions", 1))
                .machines(1, new MachineType("medium"));

        DeployPlan deployPlan = new DefaultDeployPlanGenerator().generate(request);

        Assert.assertEquals(1, deployPlan.getMachines().size());
        MachineDeploymentInfo machine0 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine0 .getMachineId());
        Assert.assertEquals(1, machine0.getInstances().size());
        assertPrimary(machine0.getInstances().get(0), 0);
    }

    @Test
    public void singlePartitionWithBackup() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "partitioned")
                        .clusterInfo("partitions", 1)
                        .clusterInfo("backupsPerPartition", 1))
                .machines(2, new MachineType("medium"));

        DeployPlan deployPlan = new DefaultDeployPlanGenerator().generate(request);

        Assert.assertEquals(2, deployPlan.getMachines().size());

        MachineDeploymentInfo machine0 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine0.getMachineId());
        Assert.assertEquals(1, machine0.getInstances().size());
        assertPrimary(machine0.getInstances().get(0), 0);

        MachineDeploymentInfo machine1 = deployPlan.getMachines().get(1);
        Assert.assertEquals(1, machine1.getMachineId());
        Assert.assertEquals(1, machine1.getInstances().size());
        assertBackup(machine1.getInstances().get(0), 0);
    }

    @Test
    public void singlePartitionWithBackupSameMachine() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "partitioned")
                        .clusterInfo("partitions", 1)
                        .clusterInfo("backupsPerPartition", 1))
                .machines(1, new MachineType("medium"));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail();
        } catch (IllegalStateException e) {
            System.out.println("Intercepted expected exception - " + e.getMessage());
        }

    }

    @Test
    public void twoPartitionsWithBackup() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "partitioned")
                        .clusterInfo("partitions", 2)
                        .clusterInfo("backupsPerPartition", 1))
                .machines(2, new MachineType("medium"));

        DeployPlan deployPlan = new DefaultDeployPlanGenerator().generate(request);

        Assert.assertEquals(2, deployPlan.getMachines().size());

        MachineDeploymentInfo machine0 = deployPlan.getMachines().get(0);
        Assert.assertEquals(0, machine0.getMachineId());
        Assert.assertEquals(2, machine0.getInstances().size());
        assertPrimary(machine0.getInstances().get(0), 0);
//        assertBackup(machine0.getInstances().get(1), 1);

        MachineDeploymentInfo machine1 = deployPlan.getMachines().get(1);
        Assert.assertEquals(1, machine1.getMachineId());
        Assert.assertEquals(2, machine1.getInstances().size());
        assertPrimary(machine1.getInstances().get(0), 1);
        assertBackup(machine1.getInstances().get(1), 0);
    }

    private static void assertInstance(InstanceDeploymentInfo instance, int partitionId, boolean primary) {
        Assert.assertEquals("foo", instance.getModuleName());
        Assert.assertEquals(partitionId, instance.getTag("partitionId"));
        Assert.assertEquals(primary, instance.getTag("primary"));
    }

    private static void assertPrimary(InstanceDeploymentInfo instance, int partitionId) {
        assertInstance(instance, partitionId, true);
    }

    private static void assertBackup(InstanceDeploymentInfo instance, int partitionId) {
        assertInstance(instance, partitionId, false);
    }

}
