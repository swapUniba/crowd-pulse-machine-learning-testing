package com.github.swapUniba.pulse.crowd.machinelearning.testing;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.spi.IPlugin;
import com.github.frapontillo.pulse.util.PulseLogger;
import org.apache.logging.log4j.Logger;
import rx.Observable;
import rx.Subscriber;
import rx.observers.SafeSubscriber;

public class MachineLearningTestingPlugin extends IPlugin<Message,Message,MachineLearningTestingConfig> {

    private static final String PLUGIN_NAME = "machine-learning-testing";
    public static final Logger logger = PulseLogger.getLogger(MachineLearningTestingPlugin.class);

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public MachineLearningTestingConfig getNewParameter() {
        return new MachineLearningTestingConfig();
    }

    @Override
    protected Observable.Operator<Message, Message> getOperator(MachineLearningTestingConfig machineLearningTestingConfig) {
        return subscriber -> new SafeSubscriber<>(new Subscriber<Message>() {

            @Override
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                logger.error("ERRORE:" + e.toString());
                subscriber.onError(e);
            }

            @Override
            public void onNext(Message message) {
                TestModel tm = new TestModel(machineLearningTestingConfig,message);
                subscriber.onNext(message);
            }
        });
    }
}
