package com.github.swapUniba.pulse.crowd.machinelearning.testing.utils;

import java.util.*;

import com.github.frapontillo.pulse.crowd.data.entity.*;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.MachineLearningTestingConfig;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.modelTesting.TestModel;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.modelTesting.TestModelSimulation;

public class Main {

    public static void main(String[] args) {

        TestSingleInstance();
        //TestTestingSet();

    }

    private static void TestSingleInstance() {
        MachineLearningTestingConfig cfg = new MachineLearningTestingConfig();
        cfg.setModelName("modello");

        for (int i = 0; i<100; i++) {

            Message msg = new Message();
            Random rndm = new Random();
            int nWords = rndm.nextInt(3)+1;
            List<Token> tokens = new ArrayList<>();
            List<String> customTags = new ArrayList<>();
            Set<Tag> tags = new HashSet<>();
            customTags.add(getRandomString());

            for (int ii = 0; ii < nWords;ii++) {
                tokens.add(new Token(getRandomString()));
            }

            for (int ii = 0; ii < nWords;ii++) {

                Tag tg = new Tag();

                tg.setText(getRandomString());
                Set<Category> ctgs = new HashSet<>();
                Category ctg = new Category();
                ctg.setText("Categoria:" + getRandomString());
                ctgs.add(ctg);
                tg.setCategories(ctgs);
                tags.add(tg);
            }

            msg.setSentiment(rndm.nextDouble());
            msg.setTokens(tokens);
            msg.setLanguage("en");
            msg.setLongitude(rndm.nextDouble());
            msg.setLatitude(rndm.nextDouble());
            msg.setFavs(rndm.nextInt());
            msg.setCustomTags(customTags);
            msg.setDate(new Date());
            msg.setTags(tags);
            msg.setShares(rndm.nextInt());
            TestModel tm = new TestModel(cfg,msg);

            Object result = tm.RunTesting();

        }
    }

    private static void TestTestingSet() {
        MachineLearningTestingConfig cfg = new MachineLearningTestingConfig();
        cfg.setModelName("modello");
        List<Entity> messages = new ArrayList<>();

        for (int i = 0; i<100; i++) {

            Message msg = new Message();
            Random rndm = new Random();
            int nWords = rndm.nextInt(3)+1;
            List<Token> tokens = new ArrayList<>();

            for (int ii = 0; ii < nWords;ii++) {
                tokens.add(new Token(getRandomString()));
            }

            msg.setSentiment(rndm.nextDouble());
            msg.setLatitude(rndm.nextDouble());
            msg.setLongitude(rndm.nextDouble());
            msg.setLanguage("en");
            msg.setTokens(tokens);
            messages.add(msg);
        }

        TestModelSimulation tm = new TestModelSimulation(cfg,messages);
        tm.RunTestingSimulation();

    }

    private static String getRandomString() {
        //char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] chars = "ab".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < random.nextInt(2)+1; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

}
