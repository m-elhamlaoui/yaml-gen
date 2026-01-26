# CI/CD Pipeline Generator

A Model-Driven Engineering (MDE) based automatic GitHub Actions workflow generator.

## Description

This project automatically generates GitHub Actions workflow files (`.yml`) from an abstract pipeline definition. It detects the project type (Java Maven, Java Gradle, Node.js) and applies the appropriate CI/CD steps based on external YAML preset files.

## Objectives

- Automate the generation of CI/CD pipelines using Model-Driven Engineering principles
- Provide a flexible and extensible architecture for supporting multiple project types
- Separate pipeline logic from technology-specific configurations using external presets
- Transform abstract pipeline models into concrete GitHub Actions workflows

## Problem Statement

Creating CI/CD pipelines manually for different project types is repetitive and error-prone. Each technology (Java, Node.js, Python, etc.) requires different build tools, commands, and configurations. This project addresses this problem by:

1. **Detecting** the project type automatically based on configuration files
2. **Loading** technology-specific steps from external preset files
3. **Generating** complete GitHub Actions workflows through model transformations

## Transformations

### M2M - Model to Model (ETL)

Converts the input model (`input.ecore`) into a pipeline model (`pipeline.ecore`) using Epsilon Transformation Language (ETL). The transformation injects technology-specific steps from the loaded preset into each job.

- **File:** `src/main/resources/transformations/input2pipeline.etl`
- **Input:** Flexmi model conforming to `input.ecore`
- **Output:** EMF model conforming to `pipeline.ecore`

### M2T - Model to Text (EGL)

Generates the final GitHub Actions YAML file from the pipeline model using Epsilon Generation Language (EGL).

- **File:** `src/main/resources/transformations/pipeline2yaml.egl`
- **Input:** Pipeline model (`pipeline.ecore`)
- **Output:** `src/main/resources/output/generated-workflow.yml`
  
## Metamodels

### Input Metamodel (`input.ecore`)

This metamodel defines the input structure of the pipeline (jobs, events, branches, environment variables).

![Input Metamodel](imgs/input.png)

---

### Pipeline Metamodel (`pipeline.ecore`)

This metamodel defines the structure of the generated GitHub Actions workflow.

![Pipeline Metamodel](imgs/pipline.png)

---

## Supported Project Types

| Type | Detected File | Preset Used |
|------|---------------|-------------|
| Java Maven | `pom.xml` | `java-maven.yml` |
| Java Gradle | `build.gradle` | `java-gradle.yml` |
| Node.js | `package.json` | `node-npm.yml` |

## Output Example

### Console Input

```
Pipeline name: my-app
Jobs: build, test
Events: push (main), pull_request (dev)
```

### Generated Output (`generated-workflow.yml`)

```yaml
name: my-app

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - dev

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3
      - name: Set up Node.js
        uses: actions/setup-node@v4
        with:
          node-version: 20
      - name: Install dependencies
        run: npm ci
      - name: Build project
        run: npm run build --if-present
```

## 🔍 Retours d'utilisation
### Ayman ANNA
Pertinence de l’approche MDE
En tant qu’utilisateur, l’idée d’automatiser la génération des workflows GitHub Actions avec une approche MDE est vraiment intéressante. Elle évite de refaire sans cesse les mêmes configurations CI/CD. Le fonctionnement est facile à comprendre : on part d’un modèle simple et l’outil génère automatiquement un workflow YAML prêt à l’emploi.

Qualité de la conception
Ce qui est appréciable, c’est que l’application est bien organisée. On sent que la logique du pipeline est séparée des détails techniques, ce qui rend l’outil plus clair et plus facile à faire évoluer.

La détection automatique du type de projet (Maven, Gradle, Node, etc.) est un vrai plus, car elle permet d’utiliser l’outil immédiatement. Les presets externes sont aussi pratiques, puisqu’ils permettent de suivre de bonnes pratiques CI/CD sans avoir à modifier l’application. De plus, l’utilisation d’ETL et d’EGL rend la génération des workflows fiable.

Utilisation en pratique
À l’usage, l’application simplifie vraiment le travail. On n’a plus besoin de chercher des fichiers YAML sur Internet ni de les adapter à chaque projet. Il suffit de fournir le projet, et le workflow est généré automatiquement, ce qui fait gagner du temps et évite des erreurs.

Conclusion
Du point de vue de l’utilisateur, ce projet est très utile. Il rend la mise en place de GitHub Actions plus simple et plus accessible. Avec quelques évolutions, comme le support d’autres plateformes CI/CD ou une validation du YAML généré, l’outil pourrait devenir encore plus intéressant au quotidien.
