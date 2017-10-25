package com.github.swapUniba.pulse.crowd.machinelearning.testing;

import com.github.frapontillo.pulse.spi.IPluginConfig;
import com.github.frapontillo.pulse.spi.PluginConfigHelper;
import com.google.gson.JsonElement;

public class MachineLearningTestingConfig implements IPluginConfig<MachineLearningTestingConfig> {

    private String modelName;
    private String options;
    private boolean isSimulation;

    @Override
    public MachineLearningTestingConfig buildFromJsonElement(JsonElement jsonElement) {
        return PluginConfigHelper.buildFromJson(jsonElement, MachineLearningTestingConfig.class);
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public boolean isSimulation() {
        return isSimulation;
    }

    public void setSimulation(boolean simulation) {
        isSimulation = simulation;
    }
}
