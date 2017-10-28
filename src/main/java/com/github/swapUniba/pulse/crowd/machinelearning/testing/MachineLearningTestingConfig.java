package com.github.swapUniba.pulse.crowd.machinelearning.testing;

import com.github.frapontillo.pulse.spi.IPluginConfig;
import com.github.frapontillo.pulse.spi.PluginConfigHelper;
import com.google.gson.JsonElement;

public class MachineLearningTestingConfig implements IPluginConfig<MachineLearningTestingConfig> {

    private String modelName;
    private String evaluation;
    private boolean isSimulation;
    private boolean printFile = false;

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

    public boolean isSimulation() {
        return isSimulation;
    }

    public void setSimulation(boolean simulation) {
        isSimulation = simulation;
    }

    public boolean isPrintFile() {
        return printFile;
    }

    public void setPrintFile(boolean printFile) {
        this.printFile = printFile;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
}
