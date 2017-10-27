package com.github.swapUniba.pulse.crowd.machinelearning.testing.utils;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Tag;
import com.github.frapontillo.pulse.crowd.data.entity.Token;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.enums.MessageFeatures;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

public class MessageToWeka {

    private static String classAttributeName = "predictedClass";

    // Riceve un messaggio, ne estrae le features rilevanti per il modello per poterlo classificare
    public static Instances getInstancesFromMessagesTest(List<Message> messages, Instances structure, String[] features, String modelName) {

        Instances result = structure;

        result.setClassIndex(structure.numAttributes() - 1);

        for (Message m : messages) {

            Instance inst = new DenseInstance(structure.numAttributes());
            inst.setDataset(result);

            for (String feature : features) {

                Enumeration<Attribute> attrEnum = structure.enumerateAttributes();

                while (attrEnum.hasMoreElements()) {

                    Attribute attr = attrEnum.nextElement();

                    if (!attr.name().toLowerCase().startsWith(classAttributeName.toLowerCase())) { // scansiona tutti gli attr, tranne quello di classe

                        MessageFeatures msgFeature = MessageFeatures.valueOf(feature.toLowerCase());

                        int attrIndex = structure.attribute(attr.name()).index();

                        if (msgFeature == MessageFeatures.cluster_kmeans && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object cluster = m.getClusterKmeans();
                            if (cluster != null) {
                                inst.setValue(attrIndex, m.getClusterKmeans());
                            }
                        } else if (msgFeature == MessageFeatures.sentiment && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object sentiment = m.getSentiment();
                            if (sentiment != null) {
                                inst.setValue(attrIndex, m.getSentiment());
                            }
                        } else if (msgFeature == MessageFeatures.number_cluster && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object cluster = m.getCluster();
                            if (cluster != null) {
                                inst.setValue(attrIndex, m.getCluster());
                            }
                        } else if (msgFeature == MessageFeatures.language && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            try {
                                Object language = m.getLanguage();
                                if (language != null) {
                                    inst.setValue(attrIndex, m.getLanguage());
                                }
                            } catch (Exception ex) {
                                // stringa non presente nel modello
                            }
                        } else if (msgFeature == MessageFeatures.shares && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object shares = m.getShares();
                            if (shares != null) {
                                inst.setValue(attrIndex, m.getShares());
                            }
                        } else if (msgFeature == MessageFeatures.favs && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object favs = m.getFavs();
                            if (favs != null) {
                                inst.setValue(attrIndex, m.getFavs());
                            }
                        } else if (msgFeature == MessageFeatures.latitude && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object latitude = m.getLatitude();
                            if (latitude != null) {
                                inst.setValue(attrIndex, m.getLatitude());
                            }
                        } else if (msgFeature == MessageFeatures.longitude && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object longitude = m.getLongitude();
                            if (longitude != null) {
                                inst.setValue(attrIndex, m.getLongitude());
                            }
                        } else if (msgFeature == MessageFeatures.text && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object text = m.getText();
                            if (text != null) {
                                inst.setValue(attrIndex, m.getText());
                            }
                        } else if (msgFeature == MessageFeatures.source && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object source = m.getSource();
                            if (source != null) {
                                inst.setValue(attrIndex, m.getSource());
                            }
                        } else if (msgFeature == MessageFeatures.fromUser && attr.name().equalsIgnoreCase(msgFeature.toString())) {
                            Object fromUser = m.getFromUser();
                            if (fromUser != null) {
                                inst.setValue(attrIndex, m.getFromUser());
                            }
                        } else {

                            if (msgFeature == MessageFeatures.tags && attr.name().startsWith("tg_")) {
                                setPresenceOfAttribute(inst, structure, attr, feature, m);
                            }
                            if (msgFeature == MessageFeatures.tokens && attr.name().startsWith("tk_")) {
                                setPresenceOfAttribute(inst, structure, attr, feature, m);
                            }
                            if (msgFeature == MessageFeatures.refUsers && attr.name().startsWith("ru_")) {
                                setPresenceOfAttribute(inst, structure, attr, feature, m);
                            }
                            if (msgFeature == MessageFeatures.toUsers && attr.name().startsWith("tu_")) {
                                setPresenceOfAttribute(inst, structure, attr, feature, m);
                            }

                        }
                    }
                }
            }

            result.add(inst);

        }

        return result;
    }

    private static void setPresenceOfAttribute(Instance inst, Instances structure, Attribute attr, String feature, Message m) {
        List<String> wordsInMessage = getWordsFromMessage(m, MessageFeatures.valueOf(feature.toLowerCase()));
        if (wordsInMessage.indexOf(attr.name()) == -1) {
            try {
                if (inst.value(attr) != 1) {
                    inst.setValue(structure.attribute(attr.name()).index(), 0);
                }
            } catch (Exception ex) {
                inst.setValue(structure.attribute(attr.name()).index(), 0);
                String a = "";
            }
        } else {
            try {
                inst.setValue(structure.attribute(attr.name()).index(), 1);
            } catch (Exception ex) {
                String a = "";// attributo non presente nel messaggio
            }
        }
    }

    private static List<String> getWordsFromMessage(Message message, MessageFeatures feature) {

        List<String> result = new ArrayList<>();

        if (feature == MessageFeatures.tokens) {
            List<Token> tokens = message.getTokens();
            if (tokens != null) {
                for (Token tk : tokens) {
                    result.add("tk_" + tk.getText());
                }
            }
        }
        if (feature == MessageFeatures.tags) {
            Set<Tag> tags = message.getTags();
            if (tags != null) {
                for (Tag tg : tags) { //ESCLUDE I TAG DI TRAINING E DI TESTING
                    if (!tg.getText().toLowerCase().startsWith("training_") && !tg.getText().toLowerCase().startsWith("testing_")) {
                        result.add("tg_" + tg.getText());
                    }
                }
            }
        }
        if (feature == MessageFeatures.toUsers) {
            List<String> users = message.getToUsers();
            for (String usr : users) {
                result.add("tu_" + usr);
            }
        }
        if (feature == MessageFeatures.refUsers) {
            List<String> users = message.getRefUsers();
            for (String usr : users) {
                result.add("ru_" + usr);
            }
        }
        if (feature == MessageFeatures.text) {
            result.add(message.getText());
        }
        if (feature == MessageFeatures.source) {
            result.add(message.getSource());
        }
        if (feature == MessageFeatures.fromUser) {
            result.add(message.getFromUser());
        }
        if (feature == MessageFeatures.parent) {
            result.add(message.getParent());
        }
        if (feature == MessageFeatures.language) {
            result.add(message.getLanguage());
        }

        result.removeIf(Objects::isNull);

        return result;

    }

}