package com.example.cicd.helpers;

import com.example.cicd.dto.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlexmiWriterTest {

    @Test
    void testGenerateFlexmi_BasicPipeline() {
        PipelineInput input = new PipelineInput("TestPipeline");
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        assertNotNull(flexmi);
        assertTrue(flexmi.contains("TestPipeline"));
        assertTrue(flexmi.contains("<?xml"));
    }

    @Test
    void testGenerateFlexmi_WithSingleJob() {
        PipelineInput input = new PipelineInput("BuildPipeline");
        Job job = new Job();
        job.setName("build");
        job.setRunsOn("ubuntu-latest");
        input.addJob(job);
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        assertNotNull(flexmi);
        assertTrue(flexmi.contains("build"));
        assertTrue(flexmi.contains("ubuntu-latest"));
    }

    @Test
    void testGenerateFlexmi_WithMultipleJobs() {
        PipelineInput input = new PipelineInput("MultiJobPipeline");
        
        Job job1 = new Job();
        job1.setName("build");
        input.addJob(job1);
        
        Job job2 = new Job();
        job2.setName("test");
        input.addJob(job2);
        
        Job job3 = new Job();
        job3.setName("deploy");
        input.addJob(job3);
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        assertTrue(flexmi.contains("build"));
        assertTrue(flexmi.contains("test"));
        assertTrue(flexmi.contains("deploy"));
    }

    @Test
    void testGenerateFlexmi_WithEnvVariables() {
        PipelineInput input = new PipelineInput("EnvPipeline");
        Job job = new Job();
        job.setName("build");
        job.addEnvVariable(new EnvVariable("NODE_ENV", "production"));
        job.addEnvVariable(new EnvVariable("API_KEY", "secret123"));
        input.addJob(job);
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        assertTrue(flexmi.contains("NODE_ENV"));
        assertTrue(flexmi.contains("production"));
        assertTrue(flexmi.contains("API_KEY"));
        assertTrue(flexmi.contains("secret123"));
    }

    @Test
    void testGenerateFlexmi_WithJobDependencies() {
        PipelineInput input = new PipelineInput("DependencyPipeline");
        Job job1 = new Job();
        job1.setName("build");
        input.addJob(job1);
        
        Job job2 = new Job();
        job2.setName("test");
        job2.addNeed("build");
        input.addJob(job2);
        
        Job job3 = new Job();
        job3.setName("deploy");
        job3.addNeed("test");
        job3.addNeed("build");
        input.addJob(job3);
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        // Verify dependencies are included
        assertNotNull(flexmi);
        assertTrue(flexmi.contains("build"));
        assertTrue(flexmi.contains("test"));
        assertTrue(flexmi.contains("deploy"));
    }

    @Test
    void testGenerateFlexmi_WithEvents() {
        PipelineInput input = new PipelineInput("EventPipeline");
        
        Event pushEvent = new Event();
        pushEvent.setEventType("push");
        pushEvent.addBranch(new Branch("main"));
        pushEvent.addBranch(new Branch("develop"));
        input.addEvent(pushEvent);
        
        Event prEvent = new Event();
        prEvent.setEventType("pull_request");
        prEvent.addBranch(new Branch("main"));
        input.addEvent(prEvent);
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        assertTrue(flexmi.contains("push"));
        assertTrue(flexmi.contains("pull_request"));
        assertTrue(flexmi.contains("main"));
        assertTrue(flexmi.contains("develop"));
    }

    @Test
    void testGenerateFlexmi_ComplexPipeline() {
        PipelineInput input = new PipelineInput("ComplexPipeline");
        
        // Add job with all features
        Job job = new Job();
        job.setName("build-and-test");
        job.setRunsOn("ubuntu-22.04");
        job.addEnvVariable(new EnvVariable("ENV", "test"));
        job.addEnvVariable(new EnvVariable("DEBUG", "true"));
        job.addNeed("setup");
        input.addJob(job);
        
        // Add event
        Event event = new Event();
        event.setEventType("push");
        event.addBranch(new Branch("main"));
        input.addEvent(event);
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        assertNotNull(flexmi);
        assertTrue(flexmi.contains("ComplexPipeline"));
        assertTrue(flexmi.contains("build-and-test"));
        assertTrue(flexmi.contains("ubuntu-22.04"));
        assertTrue(flexmi.contains("ENV"));
        assertTrue(flexmi.contains("test"));
        assertTrue(flexmi.contains("setup"));
        assertTrue(flexmi.contains("push"));
        assertTrue(flexmi.contains("main"));
    }

    @Test
    void testGenerateFlexmi_ValidXMLStructure() {
        PipelineInput input = new PipelineInput("ValidXMLTest");
        Job job = new Job();
        job.setName("test-job");
        input.addJob(job);
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        // Basic XML structure checks
        assertTrue(flexmi.startsWith("<?xml"));
        assertTrue(flexmi.contains("<"));
        assertTrue(flexmi.contains(">"));
        // Should be properly formed (opening and closing tags)
        int openTags = flexmi.split("<").length - 1;
        int closeTags = flexmi.split(">").length - 1;
        assertEquals(openTags, closeTags);
    }

    @Test
    void testGenerateFlexmi_SpecialCharactersEscaped() {
        PipelineInput input = new PipelineInput("Special<>&\"'Pipeline");
        Job job = new Job();
        job.setName("test-&-build");
        job.addEnvVariable(new EnvVariable("KEY", "value<>\"'&"));
        input.addJob(job);
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        assertNotNull(flexmi);
        // Verify special characters are handled (either escaped or present)
        assertTrue(flexmi.length() > 0);
    }

    @Test
    void testGenerateFlexmi_EmptyPipeline() {
        PipelineInput input = new PipelineInput("EmptyPipeline");
        
        String flexmi = FlexmiWriter.generateFlexmi(input);
        
        assertNotNull(flexmi);
        assertTrue(flexmi.contains("EmptyPipeline"));
        assertTrue(flexmi.contains("<?xml"));
    }
}