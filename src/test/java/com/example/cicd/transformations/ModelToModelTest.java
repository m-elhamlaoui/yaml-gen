package com.example.cicd.transformations;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModelToModel transformation class.
 * Tests the ETL transformation from Input model to Pipeline model.
 */
class ModelToModelTest {

    private ModelToModel modelToModel;
    private static final String TEST_INPUT_FLEXMI = "src/main/resources/samples/generated-input.flexmi";
    private static final String OUTPUT_XMI = "src/main/resources/output/generated-model-pipeline.xmi";

    @BeforeEach
    void setUp() {
        modelToModel = new ModelToModel();
    }

    @Test
    @DisplayName("Should successfully transform Flexmi input to Pipeline model")
    void testTransformSuccess() throws Exception {
        // Given
        File inputFile = new File(TEST_INPUT_FLEXMI);
        assertTrue(inputFile.exists(), "Input Flexmi file should exist");

        // When
        EmfModel result = modelToModel.transform(TEST_INPUT_FLEXMI);

        // Then
        assertNotNull(result, "Transformed model should not be null");
        assertEquals("Target", result.getName(), "Model name should be 'Target'");
    }

    @Test
    @DisplayName("Should generate output XMI file after transformation")
    void testTransformGeneratesOutputFile() throws Exception {
        // When
        modelToModel.transform(TEST_INPUT_FLEXMI);

        // Then
        File outputFile = new File(OUTPUT_XMI);
        assertTrue(outputFile.exists(), "Output XMI file should be generated");
        assertTrue(outputFile.length() > 0, "Output XMI file should not be empty");
    }

    @Test
    @DisplayName("Should throw exception for non-existent input file")
    void testTransformWithInvalidInputFile() {
        // Given
        String invalidPath = "non-existent-file.flexmi";

        // When & Then
        assertThrows(Exception.class, () -> {
            modelToModel.transform(invalidPath);
        }, "Should throw exception for non-existent input file");
    }

    @Test
    @DisplayName("Should create model with proper metamodel configuration")
    void testTransformModelConfiguration() throws Exception {
        // When
        EmfModel result = modelToModel.transform(TEST_INPUT_FLEXMI);

        // Then
        assertNotNull(result.getResource(), "Model resource should not be null");
        assertFalse(result.allContents().isEmpty(), "Model should contain elements");
    }

    @Test
    @DisplayName("Should handle transformation with valid pipeline structure")
    void testTransformPipelineStructure() throws Exception {
        // When
        EmfModel result = modelToModel.transform(TEST_INPUT_FLEXMI);

        // Then
        assertNotNull(result, "Result should not be null");
        // Verify the model contains pipeline elements
        assertTrue(result.allContents().size() > 0, "Pipeline model should have content");
    }
}

