package com.lambdastack.go.models;

import com.google.api.client.util.Key;

public class ValueStreamMap {
    @Key("current_pipeline")
    private String currentPipeline;

    @Key("levels")
    private ValueStreamLevel[] valueStreamLevels;

    public String getCurrentPipeline() {
        return currentPipeline;
    }

    public ValueStreamLevel[] getValueStreamLevels() {
        return valueStreamLevels;
    }
}
