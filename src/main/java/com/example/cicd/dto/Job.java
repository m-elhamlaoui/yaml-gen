package com.example.cicd.dto;

import java.util.ArrayList;
import java.util.List;

public class Job {
    private String name;
    private String runsOn;
    private List<EnvVariable> envVariables;
    private List<String> needs;

    public Job() {
        this.envVariables = new ArrayList<>();
        this.needs = new ArrayList<>();
    }

    public Job(String name) {
        this();
        this.name = name;
        this.runsOn = "ubuntu-latest";
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRunsOn() { return runsOn; }
    public void setRunsOn(String runsOn) { this.runsOn = runsOn; }
    public List<EnvVariable> getEnvVariables() { return envVariables; }
    public void setEnvVariables(List<EnvVariable> envVariables) { this.envVariables = envVariables; }
    public void addEnvVariable(EnvVariable env) { this.envVariables. add(env); }
    public List<String> getNeeds() { return needs; }
    public void setNeeds(List<String> needs) { this.needs = needs; }
    public void addNeed(String need) { this.needs.add(need); }
}