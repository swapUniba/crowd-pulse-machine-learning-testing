package com.github.swapUniba.pulse.crowd.machinelearning.testing;

import com.github.frapontillo.pulse.crowd.data.entity.Entity;
import com.github.frapontillo.pulse.spi.IPlugin;
import com.github.frapontillo.pulse.util.PulseLogger;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.modelTesting.TestModel;
import org.apache.logging.log4j.Logger;
import rx.Observable;
import rx.Subscriber;
import rx.observers.SafeSubscriber;

public class MachineLearningTestingPlugin extends IPlugin<Entity,Entity,MachineLearningTestingConfig> {

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
    protected Observable.Operator<Entity, Entity> getOperator(MachineLearningTestingConfig machineLearningTestingConfig) {
        return subscriber -> new SafeSubscriber<>(new Subscriber<Entity>() {

//            List<Entity> entities = new ArrayList<>();

            @Override
            public void onCompleted() {

/*                if (machineLearningTestingConfig.isSimulation()) {
                    TestModelSimulation tm = new TestModelSimulation(machineLearningTestingConfig,entities);
                    tm.RunTestingSimulation();
                }*/

                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                logger.error("ERRORE:" + e.toString());
                subscriber.onError(e);
            }

            @Override
            public void onNext(Entity entity) {

//                if (machineLearningTestingConfig.isSimulation()) {
//                    entities.add(entity);
//                }
//                else {
                  TestModel tm = new TestModel(machineLearningTestingConfig,entity);
                  entity = tm.RunTesting(); //aggiorna l'attributo classe dell'entita in base alla predizione
//                }

                subscriber.onNext(entity);
            }
        });
    }
}
