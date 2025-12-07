package com.automation;

import java.util.ArrayList;
import java.util.List;

public class AutomationEngine {
    private List<Rule> rules;
    
    public AutomationEngine() {
        this.rules = new ArrayList<>();
    }
    
    public void addRule(Rule rule) {
        rules.add(rule);
        System.out.println("Automation rule added: " + rule.getName());
    }
    
    public void removeRule(String ruleName) {
        rules.removeIf(r -> r.getName().equals(ruleName));
    }
    
    public void evaluateRules() {
        System.out.println("Evaluating automation rules...");
        for (Rule rule : rules) {
            rule.evaluate();
        }
    }
    
    public List<Rule> getRules() {
        return new ArrayList<>(rules);
    }
}