package com.gigaspaces.admin.deploy.plan;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by niv on 10/16/2016.
 */
public class InvalidPlansTests {

    @Test
    public void empty() {
        DeployPlanRequest request = new DeployPlanRequest();

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No modules were specified", e.getMessage());
        }
    }

    @Test
    public void multipleModules() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo"))
                .module(new Module("bar"));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Requests with multiple modules are currently not supported", e.getMessage());
        }
    }

    @Test
    public void missingMachineType() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo"));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No machine types were specified", e.getMessage());
        }
    }

    @Test
    public void multipleMachineTypes() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo"))
                .machines(1, new MachineType("small"))
                .machines(1, new MachineType("large"));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Requests with multiple machine types are currently not supported", e.getMessage());
        }
    }

    @Test
    public void missingClusterSchema() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo"))
                .machines(1, new MachineType("default"));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unsupported cluster schema null", e.getMessage());
        }
    }

    @Test
    public void unsupportedClusterSchema() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "bar"))
                .machines(1, new MachineType("default"));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unsupported cluster schema bar", e.getMessage());
        }
    }

    @Test
    public void missingClusterInstances() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "stateless"))
                .machines(1, new MachineType("default"));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("instances was not specified", e.getMessage());
        }
    }

    @Test
    public void invalidClusterInstances() {
        DeployPlanRequest request = new DeployPlanRequest()
                .module(new Module("foo")
                        .clusterInfo("schema", "stateless")
                        .clusterInfo("instances", -1))
                .machines(1, new MachineType("default"));

        try {
            new DefaultDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("instances cannot be less than one: -1", e.getMessage());
        }
    }
}
