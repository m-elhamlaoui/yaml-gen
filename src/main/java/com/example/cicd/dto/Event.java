package com.example.cicd.dto;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventType;
    private List<Branch> branches;

    public Event() {
        this.branches = new ArrayList<>();
    }

    public Event(String eventType) {
        this();
        this.eventType = eventType;
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public List<Branch> getBranches() { return branches; }
    public void setBranches(List<Branch> branches) { this.branches = branches; }
    public void addBranch(Branch branch) { this.branches.add(branch); }
}