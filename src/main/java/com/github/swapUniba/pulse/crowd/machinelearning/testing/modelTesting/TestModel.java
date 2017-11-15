package com.github.swapUniba.pulse.crowd.machinelearning.testing.modelTesting;

import com.github.frapontillo.pulse.crowd.data.entity.Entity;
import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Profile;
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
    private Entity entity;

    public TestModel(MachineLearningTestingConfig cfg, Entity entity) {
        this.config = cfg;
        this.entity = entity;
    }

    /**
     * Effettua il test in base al tipo di Entity passata nel costruttore
     * @return
     */
    public Entity RunTesting() {

        Entity result = null;

        if (entity.getClass() == Message.class) {
            result = RunMessageTesting();
        }
        else if (entity.getClass() == Profile.class) {
            // scrivere il test per il profilo!
        }

        return result;
    }

    private Message RunMessageTesting() {

        Classifier classifier = null;

        Message message = (Message)entity;

        try {
             classifier = WekaModelHandler.LoadModel(config.getModelName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Instances structure = WekaModelHandler.LoadInstanceStructure(config.getModelName());
        structure.setClassIndex(structure.numAttributes() - 1);
        //String[] features = WekaModelHandler.loadFeatures(config.getModelName());

        Instance instance = MessageToWeka.getInstancesFromMessagesTest(message,structure);

        List<Object> classes = new ArrayList<>();

        try {
            Enumeration<Object> classEnum = structure.classAttribute().enumerateValues();
            if (classEnum != null) {
                while (classEnum.hasMoreElements()) {
                    classes.add(classEnum.nextElement());
                }
            }
            assert classifier != null;
            Object classValue = classifier.classifyInstance(instance);
            Double d = new Double((double)classValue);
            int classPredIndex = d.intValue();

            if (message.getTags() == null) {
                message.setTags(new HashSet<>());
            }
            Set<Tag> tags = message.getTags();

            // Memorizza nei tag la classe associata, rimpiazza training con testing nella classe, per capire che è
            // stata associata dal testing
            if (classes.size() > 0) {
                String predClass = classes.get(classPredIndex).toString();
                System.out.println("Classe: " + predClass);

                Tag tagClass = new Tag();
                tagClass.setText(predClass.replace("training","testing"));
                tags.add(tagClass);
            }

            // Memorizza nei tag lo score rispetto ad ogni classe associato all'istanza
            double[] predictionScore = classifier.distributionForInstance(instance);
            for(int i = 0; i < predictionScore.length; i = i + 1)
            {
                Tag tagScore = new Tag();
                String clValue = instance.classAttribute().value(i);
                if (clValue == "") {
                    tagScore.setText("testing_" +config.getModelName() + "_score_" + Double.toString(predictionScore[i]));
                }
                else {
                    tagScore.setText(clValue.replace("training","testing") + "_score_" + Double.toString(predictionScore[i]));
                }

                tags.add(tagScore);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;

    }

}
