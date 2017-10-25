package com.github.swapUniba.pulse.crowd.machinelearning.testing.modelTesting;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Tag;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.MachineLearningTestingConfig;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.enums.Feature;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.MessageToWeka;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.WekaModelHandler;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

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
            String predClass = classes.get(classPredIndex).toString();
            System.out.println("Classe: " + predClass);

            if (message.getTags() == null) {
                message.setTags(new HashSet<>());
            }

            Set<Tag> tags = message.getTags();
            Tag tagClass = new Tag();
            tagClass.setText("testing_" + config.getModelName() + "_class_" + predClass);
            tags.add(tagClass);

            double[] predictionScore = classifier.distributionForInstance(instance);
            for(int i = 0; i < predictionScore.length; i = i + 1)
            {
                //System.out.println("Probability of class " + instance.classAttribute().value(i) + " : " + Double.toString(predictionScore[i]));
                Tag tagScore = new Tag();
                tagScore.setText("testing_" + config.getModelName() + "_class_" + instance.classAttribute().value(i) + "_score_" + Double.toString(predictionScore[i]));
                tags.add(tagScore);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;

    }

}
