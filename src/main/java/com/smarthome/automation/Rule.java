package com.smarthome.automation;

import java.util.function.BooleanSupplier;

public class Rule {
    private String name;
    private BooleanSupplier condition;
    private Runnable action;
    
    public Rule(String name, BooleanSupplier condition, Runnable action) {
        this.name = name;
        this.condition = condition;
        this.action = action;
    }
    
    public void evaluate() {
        if (condition.getAsBoolean()) {
            System.out.println("Rule '" + name + "' triggered");
            action.run();
        }
    }
    
    public String getName() {
        return name;
    }
}
