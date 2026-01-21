package com.example.cicd.transformations;

import com.example.cicd.helpers.ModelLoader;
import com.example.cicd.helpers.PresetLoader;
import com.example.cicd.helpers.ProjectDetector;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.etl.EtlModule;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;

import java.io.File;

public class ModelToModel {

    private static final String INPUT_METAMODEL = "src/main/resources/metamodels/input.ecore";
    private static final String PIPELINE_METAMODEL = "src/main/resources/metamodels/pipeline.ecore";
    private static final String ETL_FILE = "src/main/resources/transformations/input2pipeline.etl";

//    public EmfModel transform(String inputFlexmiPath) throws Exception {
//        return transform(inputFlexmiPath, ".");
//    }

    public EmfModel transform(String inputFlexmiPath, String projectPath) throws Exception {
        System.out.println("M2M transfo");

        //  type de projet
        String projectType = ProjectDetector.detectProjectType(projectPath);
        System.out.println("Project type detected is: " + projectType);

        // Charger le preset correspondant
        Map<String, Object> preset = PresetLoader.loadPreset(projectType);
        Map<String, List<Map<String, Object>>> presetForETL = PresetLoader.preparePresetForETL(preset);

        EtlModule etlModule = new EtlModule();
        etlModule.parse(new File(ETL_FILE));

        if (!etlModule.getParseProblems().isEmpty()) {
            throw new RuntimeException("ETL parsing error: " +
                    etlModule.getParseProblems().get(0).toString());
        }

        EmfModel inputModel = ModelLoader.loadFlexmiModel(
                inputFlexmiPath,
                INPUT_METAMODEL,
                "Source"
        );

        EmfModel pipelineModel = ModelLoader.createEmptyModel(
                PIPELINE_METAMODEL,
                "Target"
        );

        etlModule.getContext().getModelRepository().addModel(inputModel);
        etlModule.getContext().getModelRepository().addModel(pipelineModel);

        // Injecter le preset dans le contexte ETL comme variable globale
        etlModule.getContext().getFrameStack().put(
                org.eclipse.epsilon.eol.execute.context.Variable.createReadOnlyVariable("preset", presetForETL)
        );
        System.out.println("Preset injected into ETL context");

        etlModule.execute();

        pipelineModel.getResource().setURI(URI.createFileURI(
                new File("src/main/resources/output/generated-model-pipeline.xmi").getAbsolutePath()
        ));
        pipelineModel.getResource().save(Collections.emptyMap());

        System.out.println("M2M done");

        return pipelineModel;
    }
}