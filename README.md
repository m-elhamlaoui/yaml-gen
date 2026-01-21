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
