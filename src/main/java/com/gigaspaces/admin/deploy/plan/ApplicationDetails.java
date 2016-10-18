package com.gigaspaces.admin.deploy.plan;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDetails {
    private final String name;
    private final Map<String, ModuleDetails> modules = new HashMap<String, ModuleDetails>();

    public ApplicationDetails(String name) {
        this.name = name;
    }

    public ApplicationDetails module(ModuleDetails module) {
        this.modules.put(module.getName(), module);
        return this;
    }

    public Map<String, ModuleDetails> getModules() {
        return modules;
    }
}
