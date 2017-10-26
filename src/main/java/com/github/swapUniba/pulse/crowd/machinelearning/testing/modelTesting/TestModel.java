package com.github.swapUniba.pulse.crowd.machinelearning.testing.modelTesting;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Tag;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.MachineLearningTestingConfig;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.MessageToWeka;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.utils.WekaModelHandler;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

// SINTASSI WEKA PER INFORMAZIONI SUL TESTING http://www.cs.tufts.edu/~ablumer/weka/doc/weka.classifiers.Evaluation.html
// https://www.programcreek.com/java-api-examples/index.php?api=weka.classifiers.Evaluation

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
        String[] features = WekaModelHandler.loadFeatures(config.getModelName());

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        // prende l'unica istanza, dato che è un test puntuale
        Instance instance = MessageToWeka.getInstancesFromMessagesTest(messages,structure,features, config.getModelName()).get(0);

        List<Object> classes = new ArrayList<>();

        try {
            Enumeration<Object> classEnum = structure.classAttribute().enumerateValues();
            while (classEnum.hasMoreElements()) {
                classes.add(classEnum.nextElement());
            }

            assert classifier != null;
            Object classValue = classifier.classifyInstance(instance);
            Double d = new Double((double)classValue);
            int classPredIndex = d.intValue();
            String predClass = classes.get(classPredIndex).toString();
            System.out.println("Classe: " + predClass);

            if (message.getTags() == null) {
                message.setTags(new HashSet<>());
            }

            // Memorizza nei tag la classe associata, rimpiazza training con testing nella classe, per capire che è
            // stata associata dal testing
            Set<Tag> tags = message.getTags();
            Tag tagClass = new Tag();
            tagClass.setText(predClass.replace("training","testing"));
            tags.add(tagClass);

            // Memorizza nei tag lo score rispetto ad ogni classe associato all'istanza
            double[] predictionScore = classifier.distributionForInstance(instance);
            for(int i = 0; i < predictionScore.length; i = i + 1)
            {
                Tag tagScore = new Tag();
                tagScore.setText(instance.classAttribute().value(i).replace("training","testing") + "_score_" + Double.toString(predictionScore[i]));
                tags.add(tagScore);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;

    }

}
