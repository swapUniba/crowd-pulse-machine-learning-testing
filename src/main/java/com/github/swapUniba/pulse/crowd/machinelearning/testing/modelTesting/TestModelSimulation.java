package com.github.swapUniba.pulse.crowd.machinelearning.testing.modelTesting;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.MachineLearningTestingConfig;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.MessageToWeka;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.WekaModelHandler;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.util.*;

// SINTASSI WEKA PER INFORMAZIONI SUL TESTING http://www.cs.tufts.edu/~ablumer/weka/doc/weka.classifiers.Evaluation.html

public class TestModelSimulation {

    private MachineLearningTestingConfig config;
    private List<Message> messages;

    public TestModelSimulation(MachineLearningTestingConfig cfg, List<Message> messages) {
        this.config = cfg;
        this.messages = messages;
    }


    public void RunTestingSimulation() {

        Classifier classifier = null;
        Instances trainingInstances = null;
        Instances testingInstances = null;

        try {
            classifier = WekaModelHandler.LoadModel(config.getModelName());
            trainingInstances = WekaModelHandler.LoadTrainingSet(config.getModelName());
            trainingInstances.setClassIndex(trainingInstances.numAttributes()-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Instances structure = WekaModelHandler.LoadInstanceStructure(config.getModelName());
        structure.setClassIndex(structure.numAttributes() - 1);

        String[] attributes = WekaModelHandler.loadFeatures(config.getModelName());

        testingInstances = MessageToWeka.getInstancesFromMessagesTest(messages,structure,attributes,config.getModelName());

        try {
            Evaluation eval = new Evaluation(trainingInstances);
            eval.evaluateModel(classifier,trainingInstances,config.getOptions()); // ATTENZIONE! è il resubstitution error!
            System.out.println(eval.correct());
            System.out.println(eval.meanAbsoluteError());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}