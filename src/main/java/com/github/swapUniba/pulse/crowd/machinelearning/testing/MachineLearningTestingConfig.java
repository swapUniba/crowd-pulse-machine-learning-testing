package com.github.swapUniba.pulse.crowd.machinelearning.testing;

import com.github.frapontillo.pulse.spi.IPluginConfig;
import com.github.frapontillo.pulse.spi.PluginConfigHelper;
import com.github.swapUniba.pulse.crowd.machinelearning.testing.enums.Feature;
import com.google.gson.JsonElement;

public class MachineLearningTestingConfig implements IPluginConfig<MachineLearningTestingConfig> {

    private String modelName;
    private String feature;

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

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
