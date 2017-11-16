package com.github.swapUniba.pulse.crowd.machinelearning.testing.utils;

import com.github.swapUniba.pulse.crowd.machinelearning.testing.MachineLearningTestingPlugin;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/***
 * Salva e carica i modelli dei classificatori di Weka
 */
public class WekaModelHandler {

    //public static final String curPath = System.getProperty("user.dir") + File.separator + "models" + File.separator;
    public static final String curPath = "/opt/crowd-pulse/build/install/crowd-pulse/lib/models/";

    public static void SaveModel(String filename, Object predictiveModel) throws Exception {

        File f = new File(curPath);
        if (!f.exists() && !f.isDirectory()) {
            Files.createDirectory(Paths.get(curPath));
        }

        weka.core.SerializationHelper.write(curPath  + filename+".model", predictiveModel);
    }

    public static AbstractClassifier LoadModel(String filename) throws Exception {
        MachineLearningTestingPlugin.logger.info("PERCORSO: " + curPath+filename+".model");
        AbstractClassifier cls = (AbstractClassifier) weka.core.SerializationHelper.read(curPath + filename + ".model");
        return cls;
    }

    public static Instances LoadInstanceStructure(String modelName) {


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(curPath +modelName+ "_training.arff"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArffLoader.ArffReader arff = null;
        try {
            arff = new ArffLoader.ArffReader(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Instances data = arff.getStructure();
        data.setClassIndex(-1);

        return data;
    }


}
