package com.example.cicd.helpers;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PresetLoader {

    private static final String PRESETS_DIR = "src/main/resources/presets/";

    /**
     * Charge un preset YAML basé sur le type de projet.
     *
     * @param projectType Le type de projet (ex: java-maven, node-npm)
     * @return Map contenant les données du preset
     * @throws Exception si le fichier preset n'existe pas ou ne peut pas être lu
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadPreset(String projectType) throws Exception {
        String presetPath = PRESETS_DIR + projectType + ".yml";

        System.out.println("Loading preset: " + presetPath);

        if (!Files.exists(Paths.get(presetPath))) {
            throw new Exception("Preset file not found: " + presetPath);
        }

        Yaml yaml = new Yaml();
        try (InputStream inputStream = Files.newInputStream(Paths.get(presetPath))) {
            Map<String, Object> preset = yaml.load(inputStream);
            System.out.println("Preset loaded successfully for: " + projectType);
            return preset;
        }
    }

    /**
     * Extrait les steps pour un job spécifique du preset.
     *
     * @param preset Le preset chargé
     * @param jobType Le type de job (build, test, deploy)
     * @return Liste des steps pour ce job
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getStepsForJob(Map<String, Object> preset, String jobType) {
        Map<String, Object> jobs = (Map<String, Object>) preset.get("jobs");
        if (jobs == null) {
            return new ArrayList<>();
        }

        Map<String, Object> job = (Map<String, Object>) jobs.get(jobType);
        if (job == null) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> steps = (List<Map<String, Object>>) job.get("steps");
        return steps != null ? steps : new ArrayList<>();
    }

    /**
     * Convertit le preset en format compatible avec EOL/ETL.
     * Transforme les steps en une structure facilement consommable par ETL.
     *
     * @param preset Le preset chargé
     * @return Map formatée pour injection dans le contexte ETL
     */
    @SuppressWarnings("unchecked")
    public static Map<String, List<Map<String, Object>>> preparePresetForETL(Map<String, Object> preset) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        Map<String, Object> jobs = (Map<String, Object>) preset.get("jobs");
        if (jobs != null) {
            for (String jobType : jobs.keySet()) {
                List<Map<String, Object>> steps = getStepsForJob(preset, jobType);
                result.put(jobType, steps);
            }
        }

        return result;
    }
}
