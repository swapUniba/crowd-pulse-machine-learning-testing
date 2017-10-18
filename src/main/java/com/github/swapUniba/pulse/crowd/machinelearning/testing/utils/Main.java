package com.github.swapUniba.pulse.crowd.machinelearning.testing.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Token;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.MachineLearningTestingConfig;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.testingModel.TestModel;

public class Main {

    public static void main(String[] args) {

        MachineLearningTestingConfig cfg = new MachineLearningTestingConfig();
        cfg.setModelName("modello");
        cfg.setFeature("TOKEN");

        for (int i = 0; i<100; i++) {


        Message msg = new Message();
        Random rndm = new Random();
        int nWords = rndm.nextInt(3)+1;
        List<Token> tokens = new ArrayList<>();

        for (int ii = 0; ii < nWords;ii++) {
            tokens.add(new Token(getRandomString()));
        }

        msg.setTokens(tokens);

        TestModel tm = new TestModel(cfg,msg);

        Object result = tm.RunTesting();
        //System.out.println(result.toString());
        }
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
