package com.github.swapUniba.pulse.crowd.machinelearning.testing;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.enums.Feature;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.MessageToWeka;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.WekaModelHandler;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

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
        Instance instance = MessageToWeka.getSingleInstanceFromMessage(structure,message, Feature.valueOf(config.getFeature().toUpperCase()));

        try {
            Object classValue = classifier.classifyInstance(instance);
            System.out.println(classValue.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;

    }

}
