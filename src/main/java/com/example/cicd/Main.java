package com.example.cicd;

import com.example.cicd.dto.*;
import com.example.cicd.helpers.*;
import com.example.cicd.transformations.*;
import org. eclipse.epsilon.emc.emf.EmfModel;

public class Main {

    public static void main(String[] args) {
        try {
            System. out.println("CI/CD Pipeline Generator Started\n");

            PipelineInput input = createSampleInput();
            System.out.println("Created pipeline input");

            String flexmiXml = FlexmiWriter.generateFlexmi(input);
            String flexmiPath = "src/main/resources/samples/generated-input.flexmi";
            FileManager.writeFile(flexmiPath, flexmiXml);
            System.out.println("Generated Flexmi model\n");

            ModelToModel m2m = new ModelToModel();
            EmfModel pipelineModel = m2m.transform(flexmiPath);
            System.out.println();

            ModelToText m2t = new ModelToText();
            String yamlOutput = m2t. generate(pipelineModel);
            System.out.println();

            System.out.println("GENERATED GITHUB ACTIONS WORKFLOW");
            System.out.println(yamlOutput);

            System.out. println("PIPELINE COMPLETED");

        } catch (Exception e) {
            System.err. println("\nERROR OCCURRED:");
            System.err. println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static PipelineInput createSampleInput() {
        PipelineInput input = new PipelineInput("CI/CD Pipeline");

        Job buildJob = new Job("build");
        buildJob.addEnvVariable(new EnvVariable("JAVA_HOME", "/usr/lib/jvm/java-11"));
        buildJob.addEnvVariable(new EnvVariable("MAVEN_OPTS", "-Xmx1024m"));
        input.addJob(buildJob);

        Job testJob = new Job("test");
        testJob.addNeed("build");
        input.addJob(testJob);

        Job deployJob = new Job("deploy");
        deployJob.addNeed("test");
        input.addJob(deployJob);

        Event pushEvent = new Event("push");
        pushEvent.addBranch(new Branch("main"));
        pushEvent.addBranch(new Branch("develop"));
        input.addEvent(pushEvent);

        Event prEvent = new Event("pull_request");
        prEvent.addBranch(new Branch("main"));
        input.addEvent(prEvent);

        return input;
    }
}