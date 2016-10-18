package com.gigaspaces.admin.deploy.plan.impl;

import com.gigaspaces.admin.deploy.plan.*;

import java.util.Map;

public class DefaultApplicationDeployPlanGenerator extends ApplicationDeployPlanGenerator {

    @Override
    public ApplicationDeployPlan generate(ApplicationDeployPlanRequest request) {
        validate(request);
        // TODO: Support multiple modules
        ModuleDetails module = request.getApplicationDetails().getModules().values().iterator().next();
        // TODO: Support multiple machine types
        final MachineType machineType = request.getMachines().keySet().iterator().next();
        final Integer numOfMachines = request.getMachines().get(machineType);
        return generateModulePlan(module, machineType, numOfMachines);
    }

    protected void validate(ApplicationDeployPlanRequest request) {
        final ApplicationDetails application = request.getApplicationDetails();
        if (application == null)
            throw new IllegalArgumentException("Application details were not specified");
        if (application.getModules().isEmpty())
            throw new IllegalArgumentException("No modules were specified");
        if (application.getModules().size() != 1)
            throw new UnsupportedOperationException("Requests with multiple modules are currently not supported");
        if (request.getMachines().isEmpty())
            throw new IllegalArgumentException("No machine types were specified");
        if (request.getMachines().size() != 1)
            throw new UnsupportedOperationException("Requests with multiple machine types are currently not supported");
    }

    private ApplicationDeployPlan generateModulePlan(ModuleDetails module, MachineType machineType, int numOfMachines) {
        MachineCapabilities[] machinesCapabilities = toMachinesCapabilities(machineType, numOfMachines);

        final String schema = module.getClusterInfo().getSchema();
        FooProcessor processor;
        if ("stateless".equalsIgnoreCase(schema))
            processor = new StatelessProcessor(module, machinesCapabilities);
        else if ("partitioned".equalsIgnoreCase(schema))
            processor = new PartitionedProcessor(module, machinesCapabilities);
        else
            throw new IllegalArgumentException("Unsupported cluster schema " + schema);
        return processor.process();
    }

    private int nextMachine(int machineId, int machines) {
        return machineId + 1 < machines ? machineId + 1 : 0;
    }

    private MachineCapabilities[] toMachinesCapabilities(MachineType machineType, int numOfMachines) {
        MachineCapabilities[] machinesCapabilities = new MachineCapabilities[numOfMachines];
        for (int i=0 ; i < numOfMachines ; i++)
            machinesCapabilities[i] = machineType.getCapabilities().clone();
        return machinesCapabilities;
    }

    protected void processRequirements(InstanceRequirements requirements, MachineCapabilities capabilities) {
        for (Map.Entry<String, Object> requirement : requirements.getRequirements().entrySet()) {
            final String key = requirement.getKey();
            final Object required = requirement.getValue();
            final Object available = capabilities.get(key);
            if (available == null)
                throw new IllegalStateException("required capability not available on machine: " + key);
            if (required instanceof Integer) {
                Integer requiredInt = (Integer) required;
                Integer availableInt = (Integer) available;
                if (availableInt < requiredInt)
                    throw new IllegalStateException("Insufficient " + key + " capability on machine (required " + requiredInt + ", available " + availableInt + ")");
                else
                    capabilities.set(key, availableInt - requiredInt);
            }
        }
    }

    protected static int toInteger(Object value, String name) {
        if (value == null)
            throw new IllegalArgumentException(name + " was not specified");
        if (!(value instanceof Integer))
            throw new IllegalArgumentException(name + " is not an integer: " + value.getClass());
        return (Integer) value;
    }

    protected static int toPositiveInteger(Object value, String name) {
        int number = toInteger(value, name);
        if (number < 1)
            throw new IllegalArgumentException(name + " cannot be less than one: " + number);
        return number;
    }

    private abstract class FooProcessor {

        protected final ApplicationDeployPlanImpl deployPlan;
        protected final ModuleDetails module;
        protected final MachineCapabilities[] machinesCapabilities;

        protected FooProcessor(ModuleDetails module, MachineCapabilities[] machinesCapabilities) {
            this.deployPlan = new ApplicationDeployPlanImpl();
            this.module = module;
            this.machinesCapabilities = machinesCapabilities;
        }

        protected ApplicationDeployPlan process() {
            final int instances = getInstances();
            final int machines = machinesCapabilities.length;
            int machineId = 0;
            for (int instanceId=0 ; instanceId < instances ; instanceId++) {
                boolean processed = false;
                for (int i = 0; i < machines && !processed ; i++, machineId = nextMachine(machineId, machines)) {
                    processed = process(machineId, instanceId);
                }
                if (!processed)
                    throw new IllegalStateException("Failed to find a suitable machine for instance #" + instanceId);
            }
            return deployPlan;
        }

        protected abstract int getInstances();

        protected abstract boolean process(int machineId, int instanceId);
    }

    private class StatelessProcessor extends FooProcessor {

        private final int instances;

        private StatelessProcessor(ModuleDetails module, MachineCapabilities[] machinesCapabilities) {
            super(module, machinesCapabilities);
            this.instances = toPositiveInteger(module.getClusterInfo().get("instances"), "instances");
        }

        @Override
        protected int getInstances() {
            return instances;
        }

        @Override
        public boolean process(int machineId, int instanceId) {
            processRequirements(module.getInstanceRequirements(), machinesCapabilities[machineId]);
            deployPlan.addInstance(machineId, new InstanceDeployPlanImpl(instanceId, module.getName()));
            return true;
        }
    }

    private class PartitionedProcessor extends FooProcessor {

        private final int instances;
        private final int partitions;

        private PartitionedProcessor(ModuleDetails module, MachineCapabilities[] machinesCapabilities) {
            super(module, machinesCapabilities);
            this.partitions = toPositiveInteger(module.getClusterInfo().get("partitions"), "partitions");
            final int backupsPerPartition = toInteger(module.getClusterInfo().get("backupsPerPartition", 0), "backupsPerPartition");
            this.instances = partitions * (backupsPerPartition+1);
        }

        @Override
        protected int getInstances() {
            return instances;
        }

        @Override
        public boolean process(int machineId, int instanceId) {
            final Integer partitionId = instanceId % partitions;
            final Boolean primary = instanceId < partitions ? Boolean.TRUE : Boolean.FALSE;
            if (!primary) {
                if (machineId < deployPlan.getMachines().size()) {
                    for (InstanceDeployPlan instance : deployPlan.getMachines().get(machineId).getInstances()) {
                        if (instance.getTag("partitionId").equals(partitionId))
                            return false;
                    }
                }
            }
            processRequirements(module.getInstanceRequirements(), machinesCapabilities[machineId]);
            deployPlan.addInstance(machineId, new InstanceDeployPlanImpl(instanceId, module.getName())
                    .tag("partitionId", partitionId)
                    .tag("primary", primary));
            return true;
        }
    }
}
