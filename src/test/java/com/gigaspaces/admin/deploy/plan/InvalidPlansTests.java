package com.gigaspaces.admin.deploy.plan;

import com.gigaspaces.admin.deploy.plan.impl.DefaultApplicationDeployPlanGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by niv on 10/16/2016.
 */
public class InvalidPlansTests {

    @Test
    public void empty() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest();

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Application details were not specified", e.getMessage());
        }
    }

    @Test
    public void multipleModules() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                    .module(new ModuleDetails("foo"))
                    .module(new ModuleDetails("bar")));

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Requests with multiple modules are currently not supported", e.getMessage());
        }
    }

    @Test
    public void missingMachineType() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")));

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No machine types were specified", e.getMessage());
        }
    }

    @Test
    public void multipleMachineTypes() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")))
                .machines(1, new MachineType("small"))
                .machines(1, new MachineType("large"));

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Requests with multiple machine types are currently not supported", e.getMessage());
        }
    }

    @Test
    public void missingClusterSchema() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")))
                .machines(1, new MachineType("default"));

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unsupported cluster schema null", e.getMessage());
        }
    }

    @Test
    public void unsupportedClusterSchema() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")
                                .clusterInfo("schema", "bar")))
                .machines(1, new MachineType("default"));

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unsupported cluster schema bar", e.getMessage());
        }
    }

    @Test
    public void missingClusterInstances() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")
                                .clusterInfo("schema", "stateless")))
                .machines(1, new MachineType("default"));

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("instances was not specified", e.getMessage());
        }
    }

    @Test
    public void invalidClusterInstances() {
        ApplicationDeployPlanRequest request = new ApplicationDeployPlanRequest()
                .application(new ApplicationDetails("testApp")
                        .module(new ModuleDetails("foo")
                                .clusterInfo("schema", "stateless")
                                .clusterInfo("instances", -1)))
                .machines(1, new MachineType("default"));

        try {
            new DefaultApplicationDeployPlanGenerator().generate(request);
            Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("instances cannot be less than one: -1", e.getMessage());
        }
    }
}
