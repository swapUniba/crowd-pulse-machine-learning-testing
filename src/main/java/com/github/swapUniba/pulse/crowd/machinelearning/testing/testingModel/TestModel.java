package com.github.swapUniba.pulse.crowd.machinelearning.testing.testingModel;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.MachineLearningTestingConfig;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.enums.Feature;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.MessageToWeka;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.WekaModelHandler;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TestModel {

    private MachineLearningTestingConfig config;
    private Message message;

    public TestModel(MachineLearningTestingConfig cfg, Message msg) {
        this.config = cfg;
        this.message = msg;
    }


    public Message RunTesting() {

        Classifier classifier = null;

        try {
             classifier = WekaModelHandler.LoadModel(config.getModelName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Instances structure = WekaModelHandler.LoadInstanceStructure(config.getModelName());
        structure.delete(0);
        structure.setClassIndex(structure.numAttributes() - 1);
        Instance instance = MessageToWeka.getSingleInstanceFromMessage(structure,message, Feature.valueOf(config.getFeature().toUpperCase()));

        List<Object> classes = new ArrayList<>();

        try {
            Enumeration<Object> classEnum = structure.classAttribute().enumerateValues();
            while (classEnum.hasMoreElements()) {
                classes.add(classEnum.nextElement());
            }

            Object classValue = classifier.classifyInstance(instance);
            Double d = new Double((double)classValue);
            int classPredIndex = d.intValue();
            System.out.println("Classe: " + classes.get(classPredIndex).toString());
            message.setParent(classValue.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;

    }

}
