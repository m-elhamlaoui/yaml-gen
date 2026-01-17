package com.example.cicd.helpers;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;


public class ModelLoader {

    public static EmfModel loadFlexmiModel(
            String flexmiFilePath,
            String metamodelFilePath,
            String modelName) throws Exception {

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
                .put("flexmi", new FlexmiResourceFactory());

        EmfModel model = new EmfModel();
        model.setName(modelName);
        model.setMetamodelFileBased(true);
        model.setMetamodelFile(metamodelFilePath);
        model.setModelFile(flexmiFilePath);
        model.setReadOnLoad(true);
        model.setStoredOnDisposal(false);
        model.load();

        System.out.println("Loaded Flexmi model: " + modelName);
        return model;
    }

    public static EmfModel createEmptyModel(
            String metamodelFilePath,
            String modelName) throws Exception {

        EmfModel model = new EmfModel();
        model.setName(modelName);
        model.setMetamodelFileBased(true);
        model.setMetamodelFile(metamodelFilePath);
        model.setModelFileUri(org.eclipse.emf.common.util.URI.createURI("temp://" + modelName + ".xmi"));
        model.setReadOnLoad(false);
        model.setStoredOnDisposal(false);
        model.load();

        System.out.println("Created empty model: " + modelName);
        return model;
    }
}