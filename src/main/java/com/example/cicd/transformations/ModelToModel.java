package com.example.cicd.transformations;

import com.example.cicd.helpers. ModelLoader;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.etl.EtlModule;

import java.io.File;

public class ModelToModel {

    private static final String INPUT_METAMODEL = "src/main/resources/metamodels/input.ecore";
    private static final String PIPELINE_METAMODEL = "src/main/resources/metamodels/pipeline.ecore";
    private static final String ETL_FILE = "src/main/resources/transformations/input2pipeline.etl";

    public EmfModel transform(String inputFlexmiPath) throws Exception {
        System.out.println("M2M transfo");

        EtlModule etlModule = new EtlModule();
        etlModule.parse(new File(ETL_FILE));

        if (! etlModule.getParseProblems().isEmpty()) {
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

        etlModule.execute();

        System.out.println("M2M done");

        return pipelineModel;
    }
}