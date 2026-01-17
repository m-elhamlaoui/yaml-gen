package com.example.cicd. transformations;

import com.example.cicd.helpers.FileManager;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.egl.IEglModule;
import org.eclipse.epsilon.emc.emf.EmfModel;

import java.io.File;

public class ModelToText {

    private static final String EGL_TEMPLATE = "src/main/resources/transformations/pipeline2yaml.egl";
    private static final String OUTPUT_FILE = "src/main/resources/output/generated-workflow.yml";

    public String generate(EmfModel pipelineModel) throws Exception {
        System.out.println(" M2T");

        IEglModule eglModule = new EglTemplateFactoryModuleAdapter();

        String templateContent = FileManager.readFile(EGL_TEMPLATE);
        eglModule.parse(templateContent, new File(EGL_TEMPLATE));

        if (!eglModule.getParseProblems().isEmpty()) {
            throw new RuntimeException("EGL parsing error:  " +
                    eglModule.getParseProblems().get(0).toString());
        }

        eglModule.getContext().getModelRepository().addModel(pipelineModel);

        String yamlOutput = eglModule.execute().toString();

        FileManager.writeFile(OUTPUT_FILE, yamlOutput);

        System.out.println(" M2T completed");
        System.out.println(" Output saved to: " + OUTPUT_FILE);

        return yamlOutput;
    }
}