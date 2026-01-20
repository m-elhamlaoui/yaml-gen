package com.example.cicd.transformations;

import com.example.cicd.helpers.ModelLoader;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModelToText transformation class.
 * Tests the EGL transformation from Pipeline model to YAML output.
 */
class ModelToTextTest {

    private ModelToText modelToText;
    private ModelToModel modelToModel;
    private static final String TEST_INPUT_FLEXMI = "src/main/resources/samples/generated-input.flexmi";
    private static final String OUTPUT_YAML = "src/main/resources/output/generated-workflow.yml";

    @BeforeEach
    void setUp() {
        modelToText = new ModelToText();
        modelToModel = new ModelToModel();
    }

    @Test
    @DisplayName("Should successfully generate YAML from Pipeline model")
    void testGenerateSuccess() throws Exception {
        // Given - First create a pipeline model using M2M transformation
        EmfModel pipelineModel = modelToModel.transform(TEST_INPUT_FLEXMI);
        assertNotNull(pipelineModel, "Pipeline model should be created");

        // When
        String yamlOutput = modelToText.generate(pipelineModel);

        // Then
        assertNotNull(yamlOutput, "YAML output should not be null");
        assertFalse(yamlOutput.isEmpty(), "YAML output should not be empty");
    }

    @Test
    @DisplayName("Should generate output YAML file")
    void testGenerateCreatesOutputFile() throws Exception {
        // Given
        EmfModel pipelineModel = modelToModel.transform(TEST_INPUT_FLEXMI);

        // When
        modelToText.generate(pipelineModel);

        // Then
        File outputFile = new File(OUTPUT_YAML);
        assertTrue(outputFile.exists(), "Output YAML file should be created");
        assertTrue(outputFile.length() > 0, "Output YAML file should not be empty");
    }

    @Test
    @DisplayName("Should generate valid YAML content with 'name' key")
    void testGenerateYamlContainsName() throws Exception {
        // Given
        EmfModel pipelineModel = modelToModel.transform(TEST_INPUT_FLEXMI);

        // When
        String yamlOutput = modelToText.generate(pipelineModel);

        // Then
        assertTrue(yamlOutput.contains("name:"), "YAML should contain 'name:' key");
    }

    @Test
    @DisplayName("Should generate YAML with 'on' trigger section")
    void testGenerateYamlContainsTrigger() throws Exception {
        // Given
        EmfModel pipelineModel = modelToModel.transform(TEST_INPUT_FLEXMI);

        // When
        String yamlOutput = modelToText.generate(pipelineModel);

        // Then
        assertTrue(yamlOutput.contains("on:"), "YAML should contain 'on:' trigger section");
    }

    @Test
    @DisplayName("Should generate YAML with 'jobs' section")
    void testGenerateYamlContainsJobs() throws Exception {
        // Given
        EmfModel pipelineModel = modelToModel.transform(TEST_INPUT_FLEXMI);

        // When
        String yamlOutput = modelToText.generate(pipelineModel);

        // Then
        assertTrue(yamlOutput.contains("jobs:"), "YAML should contain 'jobs:' section");
    }

    @Test
    @DisplayName("Should write YAML content to file correctly")
    void testGenerateWritesToFile() throws Exception {
        // Given
        EmfModel pipelineModel = modelToModel.transform(TEST_INPUT_FLEXMI);

        // When
        String yamlOutput = modelToText.generate(pipelineModel);

        // Then
        String fileContent = Files.readString(Paths.get(OUTPUT_YAML));
        assertEquals(yamlOutput, fileContent, "File content should match generated YAML");
    }

    @Test
    @DisplayName("Should generate YAML with proper structure for GitHub Actions")
    void testGenerateGitHubActionsStructure() throws Exception {
        // Given
        EmfModel pipelineModel = modelToModel.transform(TEST_INPUT_FLEXMI);

        // When
        String yamlOutput = modelToText.generate(pipelineModel);

        // Then
        // Check for GitHub Actions specific keywords
        assertTrue(yamlOutput.contains("runs-on:") || yamlOutput.contains("runs_on:"),
                "YAML should contain runner specification");
    }

    @Test
    @DisplayName("Should handle pipeline with multiple jobs")
    void testGenerateWithMultipleJobs() throws Exception {
        // Given
        EmfModel pipelineModel = modelToModel.transform(TEST_INPUT_FLEXMI);

        // When
        String yamlOutput = modelToText.generate(pipelineModel);

        // Then
        assertNotNull(yamlOutput, "YAML output should not be null");
        // The input has build, test, deploy jobs
        assertTrue(yamlOutput.contains("build") || yamlOutput.contains("test") || yamlOutput.contains("deploy"),
                "YAML should contain job names");
    }
}

