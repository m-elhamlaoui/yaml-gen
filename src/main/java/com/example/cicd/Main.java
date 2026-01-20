package com.example.cicd;

import com.example.cicd.dto.*;
import com.example.cicd.helpers.*;
import com.example.cicd.transformations.*;
import org.eclipse.epsilon.emc.emf.EmfModel;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        try {
            System.out.println("CI/CD Pipeline Generator Started\n");

            PipelineInput input = promptPipelineInput();
            System.out.println("\nCreated pipeline input");

            String flexmiXml = FlexmiWriter.generateFlexmi(input); // java -> flexmi
            String flexmiPath = "src/main/resources/samples/generated-input.flexmi";
            FileManager.writeFile(flexmiPath, flexmiXml);
            System.out.println("Generated Flexmi model\n");

            ModelToModel m2m = new ModelToModel();
            EmfModel pipelineModel = m2m.transform(flexmiPath);

            ModelToText m2t = new ModelToText();
            String yamlOutput = m2t.generate(pipelineModel);

            System.out.println("GITHUB WORKFLOW");
            // System.out.println(yamlOutput);

        } catch (Exception e) {
            System.err.println("\nERROR OCCURRED:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private PipelineInput promptPipelineInput() {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Nom de la pipeline (ex: MyPipeline): ");
            String pipelineName = readLine(sc);

            PipelineInput input = new PipelineInput(pipelineName);

            // Jobs
            System.out.println("\n--- Jobs ---");
            while (yes(sc, "Ajouter un job ? (y/n): ")) {
                Job job = new Job();
                System.out.print("Nom du job: ");
                job.setName(readLine(sc));

                System.out.print("runsOn (laisser vide pour 'ubuntu-latest'): ");
                String runsOn = readLine(sc);
                if (runsOn != null && !runsOn.trim().isEmpty()) job.setRunsOn(runsOn);

                // env variables
                while (yes(sc, "  Ajouter une variable d'environnement ? (y/n): ")) {
                    System.out.print("    KEY: ");
                    String key = readLine(sc);
                    System.out.print("    VALUE: ");
                    String val = readLine(sc);
                    job.addEnvVariable(new EnvVariable(key, val));
                }

                // needs
                while (yes(sc, "  Ajouter une dépendance (needs) ? (y/n): ")) {
                    System.out.print("    Nom du job requis: ");
                    String need = readLine(sc);
                    job.addNeed(need);
                }

                input.addJob(job);
                System.out.println("Job ajouté: " + job.getName());
            }

            // Events
            System.out.println("\n--- Events ---");
            while (yes(sc, "Ajouter un event ? (y/n): ")) {
                Event event = new Event();
                System.out.print("Type d'event (ex: push, pull_request): ");
                event.setEventType(readLine(sc));

                while (yes(sc, "  Ajouter une branche pour cet event ? (y/n): ")) {
                    System.out.print("    Nom de la branche: ");
                    String branchName = readLine(sc);
                    event.addBranch(new Branch(branchName));
                }

                input.addEvent(event);
                System.out.println("Event ajouté: " + event.getEventType());
            }

            return input;
        } finally {

        }
    }

    // helper: read a full line (returns "" if EOF)
    private String readLine(Scanner sc) {
        String line = sc.nextLine();
        return line == null ? "" : line.trim();
    }

    // helper: interpret yes/no
    private boolean yes(Scanner sc, String prompt) {
        System.out.print(prompt);
        String ans = readLine(sc);
        if (ans.isEmpty()) return false;
        ans = ans.trim().toLowerCase();
        return ans.equals("y") || ans.equals("o") || ans.equals("oui") || ans.equals("yes");
    }
}