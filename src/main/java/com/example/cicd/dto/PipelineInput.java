package com.example.cicd.dto;

import java.util.ArrayList;
import java.util.List;

public class PipelineInput {
    private String pipelineName;
    private List<Job> jobs;
    private List<Event> events;

    public PipelineInput() {
        this.jobs = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public PipelineInput(String pipelineName) {
        this();
        this.pipelineName = pipelineName;
    }

    public String getPipelineName() { return pipelineName; }
    public void setPipelineName(String pipelineName) { this.pipelineName = pipelineName; }
    public List<Job> getJobs() { return jobs; }
    public void setJobs(List<Job> jobs) { this.jobs = jobs; }
    public void addJob(Job job) { this.jobs.add(job); }
    public List<Event> getEvents() { return events; }
    public void setEvents(List<Event> events) { this.events = events; }
    public void addEvent(Event event) { this.events.add(event); }
}