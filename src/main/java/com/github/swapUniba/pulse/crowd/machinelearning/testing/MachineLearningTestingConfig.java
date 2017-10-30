package com.github.swapUniba.pulse.crowd.machinelearning.testing;

import com.github.frapontillo.pulse.spi.IPluginConfig;
import com.github.frapontillo.pulse.spi.PluginConfigHelper;
import com.google.gson.JsonElement;

public class MachineLearningTestingConfig implements IPluginConfig<MachineLearningTestingConfig> {

    private String modelName;

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

}
