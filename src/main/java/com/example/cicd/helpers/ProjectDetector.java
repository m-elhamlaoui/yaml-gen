package com.example.cicd.helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ProjectDetector {

    private static final String DEFAULT_PROJECT_TYPE = "java-maven";

    public static String detectProjectType(String projectPath) {
        Path basePath = Paths.get(projectPath);

        // Vérifier Java/Maven (pom.xml)
        if (fileExists(basePath, "pom.xml")) {
            System.out.println("Detected project type: java-maven (found pom.xml)");
            return "java-maven";
        }

        // Vérifier Java/Gradle (build.gradle)
        if (fileExists(basePath, "build.gradle")) {
            System.out.println("Detected project type: java-gradle (found build.gradle)");
            return "java-gradle";
        }

        // Vérifier Node.js/npm (package.json)
        if (fileExists(basePath, "package.json")) {
            System.out.println("Detected project type: node-npm (found package.json)");
            return "node-npm";
        }

        System.out.println("No project file found, defaulting to: " + DEFAULT_PROJECT_TYPE);
        return DEFAULT_PROJECT_TYPE;
    }

    private static boolean fileExists(Path basePath, String fileName) {
        boolean exists = Files.exists(basePath.resolve(fileName));
        return exists;
    }
}
