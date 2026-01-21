package com.example.cicd.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PipelineInputTest {

    private PipelineInput pipelineInput;

    @BeforeEach
    void setUp() {
        pipelineInput = new PipelineInput("TestPipeline");
    }

    @Test
    void testConstructor() {
        assertEquals("TestPipeline", pipelineInput.getPipelineName());
    }

    @Test
    void testGetName() {
        PipelineInput input = new PipelineInput("MyPipeline");
        assertEquals("MyPipeline", input.getPipelineName());
    }

    @Test
    void testAddJob() {
        Job job = new Job();
        job.setName("build");
        
        pipelineInput.addJob(job);
        
        assertNotNull(pipelineInput.getJobs());
        assertEquals(1, pipelineInput.getJobs().size());
        assertTrue(pipelineInput.getJobs().contains(job));
    }

    @Test
    void testAddMultipleJobs() {
        Job job1 = new Job();
        job1.setName("build");
        Job job2 = new Job();
        job2.setName("test");
        Job job3 = new Job();
        job3.setName("deploy");
        
        pipelineInput.addJob(job1);
        pipelineInput.addJob(job2);
        pipelineInput.addJob(job3);
        
        assertEquals(3, pipelineInput.getJobs().size());
    }

    @Test
    void testAddEvent() {
        Event event = new Event();
        event.setEventType("push");
        
        pipelineInput.addEvent(event);
        
        assertNotNull(pipelineInput.getEvents());
        assertEquals(1, pipelineInput.getEvents().size());
        assertTrue(pipelineInput.getEvents().contains(event));
    }

    @Test
    void testAddMultipleEvents() {
        Event event1 = new Event();
        event1.setEventType("push");
        Event event2 = new Event();
        event2.setEventType("pull_request");
        
        pipelineInput.addEvent(event1);
        pipelineInput.addEvent(event2);
        
        assertEquals(2, pipelineInput.getEvents().size());
    }

    @Test
    void testGetJobs_InitiallyEmpty() {
        PipelineInput input = new PipelineInput("EmptyPipeline");
        assertNotNull(input.getJobs());
        assertEquals(0, input.getJobs().size());
    }

    @Test
    void testGetEvents_InitiallyEmpty() {
        PipelineInput input = new PipelineInput("EmptyPipeline");
        assertNotNull(input.getEvents());
        assertEquals(0, input.getEvents().size());
    }

    @Test
    void testComplexPipelineInput() {
        // Create jobs
        Job buildJob = new Job();
        buildJob.setName("build");
        buildJob.addEnvVariable(new EnvVariable("NODE_ENV", "production"));
        
        Job testJob = new Job();
        testJob.setName("test");
        testJob.addNeed("build");
        
        // Create events
        Event pushEvent = new Event();
        pushEvent.setEventType("push");
        pushEvent.addBranch(new Branch("main"));
        
        Event prEvent = new Event();
        prEvent.setEventType("pull_request");
        
        // Add to pipeline
        pipelineInput.addJob(buildJob);
        pipelineInput.addJob(testJob);
        pipelineInput.addEvent(pushEvent);
        pipelineInput.addEvent(prEvent);
        
        // Verify
        assertEquals("TestPipeline", pipelineInput.getPipelineName());
        assertEquals(2, pipelineInput.getJobs().size());
        assertEquals(2, pipelineInput.getEvents().size());
    }
}